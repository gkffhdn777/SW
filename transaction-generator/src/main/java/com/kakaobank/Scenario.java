package com.kakaobank;

import java.time.LocalDate;

import com.kakaobank.domain.Bank;
import com.kakaobank.domain.BankProducer;
import com.kakaobank.domain.Customer;
import com.kakaobank.domain.Money;
import com.kakaobank.domain.Registration;
import com.kakaobank.domain.banking.Deposit;
import com.kakaobank.domain.banking.ReceiverBank;
import com.kakaobank.domain.banking.Transfer;
import com.kakaobank.domain.banking.Withdrawal;
import com.kakaobank.event.RawEvent;
import com.kakaobank.infra.CustomerBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * scenarios
 */
public final class Scenario {

	private static final Logger LOGGER = LoggerFactory.getLogger(Scenario.class);

	private Scenario() {
	}

	/**
	 * 사용자가 가입 -> 계설 -> 입금 -> 출금, 이체 등의 정상 상황을 연출 한다.
	 */
	public static void success(final BankProducer<RawEvent> bankProducer, final CustomerBalanceRepository repository) {
		try {
			Customer customer = new Customer("김민석", LocalDate.of(1985, 8, 28));
			Registration member = new Registration(customer, bankProducer).createAccount();
			LOGGER.info("김민석님 회원 가입이 및 계좌 생성이 완료 되었습니다. 이제 은행을 이용 가능하십니다.");
			Bank bank = new Bank(repository, bankProducer);
			Thread.sleep(3000);

			Money money1 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(500000)));
			LOGGER.info("1. 아이디 : {}, 김민석님 입금하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(500000), money1);
			Thread.sleep(2000);
			Money money2 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(500000)));
			LOGGER.info("2. 아이디 : {}, 김민석님 입금하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(500000), money2);
			Thread.sleep(500);

			Money money3 = bank.requestToBank(new Withdrawal(member.getId(), member.getAccountNumber(), Money.wons(50000)));
			LOGGER.info("3. 아이디 : {}, 김민석님 출금하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(50000), money3);
			Thread.sleep(1000);
			Money money4 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(100000)));
			LOGGER.info("5. 아이디 : {}, 김민석님 입금하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(100000), money4);
			Thread.sleep(1000);
			Money money5 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(20000)));
			LOGGER.info("6. 아이디 : {}, 김민석님 입금하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(20000), money5);


			Thread.sleep(2000);
			Money money6 = bank.requestToBank(new Transfer(member.getId(), member.getAccountNumber(), ReceiverBank.KBBANK, "1002-1234-1234234", "홍길동", Money.wons(10000)));
			LOGGER.info("7. 아이디 : {}, 김민석님 홍길동님에게 이체하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(10000), money6);
			Thread.sleep(2000);
			Money money7 = bank.requestToBank(new Transfer(member.getId(), member.getAccountNumber(), ReceiverBank.KBBANK, "1002-5555-5432156", "장녹수", Money.wons(100000)));
			LOGGER.info("9. 아이디 : {}, 김민석님 장녹수님에게 이체하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(100000), money7);

			Thread.sleep(500);
			Money money8 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(300000)));
			LOGGER.info("아이디 : {}, 김민석님 인출하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(300000), money8);
			Thread.sleep(1000);
			Money money9 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(30000)));
			LOGGER.info("아이디 : {}, 김민석님 인출하였습니다. : {}, 김민석님 통장 잔고 : {}", member.getId(), Money.wons(30000), money9);
			Thread.sleep(1000);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 이상 감지 시나리오.
	 * 조건:
	 * 고객의 만 나이가 60세 이상인 경우
	 * 48시간 이내 신규 개설 된 계좌에 100만원 이상 입금 후, 2시간 이내 이체 (또는 출금)되어 잔액이 1만원 이하가 된 경우
	 */
	public static void fail1(final BankProducer<RawEvent> bankProducer, final CustomerBalanceRepository repository) {

		try {

			Customer customer = new Customer("황미영", LocalDate.of(1855, 12, 25));
			Registration member = new Registration(customer, bankProducer).createAccount();
			Bank bank = new Bank(repository, bankProducer);
			Thread.sleep(3000);

			Money money1 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(500000)));
			LOGGER.info("아이디 : {}, 황미영님 입금하였습니다. : {}, 황미영님 통장 잔고 : {}", member.getId(), Money.wons(500000), money1);

			Money money2 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(500000)));
			LOGGER.info("아이디 : {}, 황미영님 입금하였습니다. : {}, 황미영님 통장 잔고 : {}", member.getId(), Money.wons(500000), money2);

			Money money3 = bank.requestToBank(new Withdrawal(member.getId(), member.getAccountNumber(), Money.wons(1000000)));
			LOGGER.info("아이디 : {}, 황미영님 인출하였습니다. : {}, 황미영님 통장 잔고 : {}", member.getId(), Money.wons(1000000), money3);

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}

	public static void fail2(final BankProducer<RawEvent> bankProducer, final CustomerBalanceRepository repository) {

		try {

			Customer customer = new Customer("임꺽정", LocalDate.of(1855, 2, 3));
			Registration member = new Registration(customer, bankProducer).createAccount();
			Bank bank = new Bank(repository, bankProducer);
			Thread.sleep(3000);

			Money money1 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(500000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(500000), money1);

			Money money2 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(1000200)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(1000300), money2);

			Money money3 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(310000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(310000), money3);

			Money money4 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(10000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(10000), money4);

			Money money5 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(502000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(502000), money5);

			Money money6 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(10900000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(10900000), money6);

			Money money7 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(400000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(400000), money7);

			Money money8 = bank.requestToBank(new Deposit(member.getId(), member.getAccountNumber(), Money.wons(150000)));
			LOGGER.info("아이디 : {}, 임꺽정님 입금하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(150000), money8);
			Thread.sleep(1000);

			Money money9 = bank.requestToBank(new Transfer(member.getId(), member.getAccountNumber(), ReceiverBank.KBBANK, "1002-8888-89765431", "고소영", Money.wons(13772200)));
			LOGGER.info("아이디 : {}, 임꺽정님 고소영님에게 이체하였습니다. : {}, 임꺽정님 통장 잔고 : {}", member.getId(), Money.wons(13772200), money9);

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
	}
}
