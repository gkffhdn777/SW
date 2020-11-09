package com.kao;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

import com.kao.evaluator.application.EvaluatorConsumer;
import com.kao.evaluator.application.EvaluatorProducer;
import com.kao.evaluator.application.EvaluatorService;
import com.kao.evaluator.application.Rule;
import com.kao.evaluator.application.rule.AccountRule;
import com.kao.evaluator.application.rule.CustomerRule;
import com.kao.evaluator.application.rule.MoneyRule;
import com.kao.evaluator.application.rule.NewAccountRule;
import com.kao.evaluator.event.JsonDeserializer;
import com.kao.evaluator.event.RawEvent;
import com.kao.evaluator.store.EventStoreHandler;
import com.kao.evaluator.suport.DateUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;

public class Application extends Thread {

	private final KafkaConsumer<String, RawEvent> consumer;

	public Application(KafkaConsumer<String, RawEvent> consumer) {
		this.consumer = consumer;
	}

	private static Properties consumerProperties() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.19.136.236:9092,172.19.136.226:9092,172.19.136.231:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "evaluator-group");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));
		return properties;
	}

	@Override
	public void run() {
		Rule accountRule = new AccountRule(account -> DateUtils.isWithinOfTime(account.getCreateDate(), 48));
		Rule customerRule = new CustomerRule(registration -> DateUtils.calculateAge(registration.getBirthDate()) > 60);
		Rule moneyRule = new MoneyRule(balance -> balance <= 1000000 );
		Rule newAccountRule = new NewAccountRule(time -> time <= 2);
			new EvaluatorConsumer(
					new EvaluatorService(new EvaluatorProducer(), Arrays.asList(accountRule, customerRule, moneyRule, newAccountRule)),
					new EventStoreHandler(),
					consumer
			).start();
	}

	public static void main(String[] args) throws InterruptedException {
		//Application application = new Application();
		//Thread.sleep(1000);
		//Runtime.getRuntime().addShutdownHook(application);
	}
}

