package com.example.cqrsddd.infra.persistance;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.H2)
interface JdbcTeamUserRepository extends CrudRepository<TeamUserEntity, Object> {
}
