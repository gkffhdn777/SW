package com.kakaobank.infra;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.kakaobank.domain.AccountNumber;
import com.kakaobank.domain.CustomerBalance;
import com.kakaobank.domain.CustomerId;
import com.kakaobank.domain.Money;

public final class CustomerBalanceRepository implements CustomerBalance {

	private final Map<CustomerId, Map<AccountNumber, Money>> balance;

	public CustomerBalanceRepository(final Map<CustomerId, Map<AccountNumber, Money>> balance) {
		this.balance = balance;
	}

	public Map<AccountNumber, Money> save(final CustomerId id, final AccountNumber accountNumber, final Money money) {
		checkNull(id, accountNumber, money);
		ConcurrentHashMap<AccountNumber, Money> accountNumberMoney = new ConcurrentHashMap<>();
		accountNumberMoney.putIfAbsent(accountNumber, money);
		balance.putIfAbsent(id, accountNumberMoney);
		return accountNumberMoney;
	}

	@Override
	public Money plus(final CustomerId id, final AccountNumber accountNumber, final Money money) {
		checkNull(id, accountNumber, money);
		if (balance.get(id) == null || balance.get(id).isEmpty()) {
			return save(id, accountNumber, money).get(accountNumber);
		}
		return balance.computeIfPresent(id, (k, v) -> {
			v.computeIfPresent(accountNumber, (k2, v2) -> v2.plus(money));
			return v;
		}).get(accountNumber);
	}

	@Override
	public Money minus(final CustomerId id, final AccountNumber accountNumber, final Money money) {
		checkNull(id, accountNumber, money);
		Money resultMoney = balance.computeIfPresent(id, (k, v) -> {
			v.computeIfPresent(accountNumber, (k2, v2) -> v2.minus(money));
			return v;
		}).get(accountNumber);

		if (resultMoney.getAmount().compareTo(BigDecimal.ZERO) < 0) {
			throw new CustomerBalanceException("The balance is insufficient.");
		}
		return resultMoney;
	}

	@Override
	public Money findByCustomerId(final CustomerId id, final AccountNumber accountNumber) {
		return Optional.ofNullable(balance.get(id)).map(x -> x.get(accountNumber)).orElse(Money.ZERO);
	}

	private void checkNull(final CustomerId id, final AccountNumber no, final Money money) {
		if (id == null) {
			throw new NullPointerException("Id cannot be null.");
		}
		if (no == null) {
			throw new NullPointerException("AccountNumber cannot be null.");
		}
		if (money == null) {
			throw new NullPointerException("Money cannot be null.");
		}
	}
}
