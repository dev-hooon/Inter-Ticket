package dev.hooon.common.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@Profile("test")
public class DatabaseCleaner {

	private final List<String> tableNames = new ArrayList<>();

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void findDatabaseTableNames() {
		List<String> tableNamesInMethod = entityManager.createNativeQuery(
				"SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema';")
			.getResultList();
		this.tableNames.addAll(tableNamesInMethod);
	}

	private void truncate() {
		entityManager.createNativeQuery("SET session_replication_role = replica;").executeUpdate();
		for (String tableName : tableNames) {
			entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s CASCADE", tableName)).executeUpdate();
		}
		entityManager.createNativeQuery("SET session_replication_role = DEFAULT;").executeUpdate();
	}

	@Transactional
	public void clear() {
		entityManager.clear();
		truncate();
	}
}
