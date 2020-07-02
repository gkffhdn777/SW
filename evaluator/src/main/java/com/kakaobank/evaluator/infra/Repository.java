package com.kakaobank.evaluator.infra;

import java.util.Spliterator;

import org.bson.conversions.Bson;

public interface Repository {

	<T> Boolean save(T t, Class<T> clazz);

	<T> Spliterator<T> find(Bson filter, Class<T> clazz);

	<T> T findOne(Class<T> clazz);

	<T> T findOne(Bson filter, Class<T> clazz);
}
