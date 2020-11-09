package com.kao.evaluator.event;

import com.kao.evaluator.application.Event;

public enum BankActionType {
	ACCOUNT {
		public Account getEvent(String payload) {
			return JsonMapper.getInstance().converterToEventMessage(payload, Account.class);
		}
	}, REGISTRATION {
		@Override
		public Registration getEvent(String payload) {
			return JsonMapper.getInstance().converterToEventMessage(payload, Registration.class);
		}
	}, DEPOSIT {
		@Override
		public Deposit getEvent(String payload) {
			return JsonMapper.getInstance().converterToEventMessage(payload, Deposit.class);
		}
	}, TRANSFER {
		@Override
		public Transfer getEvent(String payload) {
			return JsonMapper.getInstance().converterToEventMessage(payload, Transfer.class);
		}
	}, WITHDRAWAL {
		@Override
		public Withdrawal getEvent(String payload) {
			return JsonMapper.getInstance().converterToEventMessage(payload, Withdrawal.class);
		}
	};

	public abstract Event getEvent(String payload);
}
