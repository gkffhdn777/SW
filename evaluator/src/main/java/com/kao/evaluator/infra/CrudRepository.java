package com.kao.evaluator.infra;

import java.util.Arrays;
import java.util.Spliterator;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CrudRepository implements Repository {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrudRepository.class);

	private static final String HOST = "localhost";

	private static final Integer PORT = 32768;

	private static final String DATABASE_NAME = "bank-customer-events";

	private CrudRepository() {
	}

	private static class LazyHolder {
		public static final CrudRepository INSTANCE = new CrudRepository();
	}

	public static CrudRepository getInstance() {
		return LazyHolder.INSTANCE;
	}

	private MongoClient mongoClient() {
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
		return MongoClients.create(
				MongoClientSettings.builder()
						.applyToClusterSettings(builder ->
								builder.hosts(Arrays.asList(new ServerAddress(HOST, PORT))))
						.codecRegistry(codecRegistry)
						.build());
	}

	@Override
	public <T> Boolean save(T t, Class<T> clazz) {
		try (MongoClient mongoClient = mongoClient()) {
			MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
			MongoCollection<T> collection = database.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
			collection.insertOne(t);
		} catch (Exception ex) {
			LOGGER.warn("Mongodb error and default return false.", ex);
			return false;
		}
		return true;
	}

	@Override
	public <T> Spliterator<T> find(Bson filter, Class<T> clazz) {
		try (MongoClient mongoClient = mongoClient()) {
			MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
			MongoCollection<T> collection = database.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
			return collection.find(filter).spliterator();
		} catch (Exception ex) {
			throw new MonoRepositoryException(ex);
		}
	}

	@Override
	public <T> T findOne(Bson filter, Class<T> clazz) {
		try (MongoClient mongoClient = mongoClient()) {
			MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
			MongoCollection<T> collection = database.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
			return collection.find(filter).first();
		} catch (Exception ex) {
			throw new MonoRepositoryException(ex);
		}
	}

	@Override
	public <T> T findOne(Class<T> clazz) {
		try (MongoClient mongoClient = mongoClient()) {
			MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
			MongoCollection<T> collection = database.getCollection(clazz.getSimpleName().toLowerCase(), clazz);
			return collection.find().first();
		} catch (Exception ex) {
			throw new MonoRepositoryException(ex);
		}
	}
}
