package com.ruczajsoftware.workoutrival.config;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruczajsoftware.workoutrival.model.database.DbCollectionsKt.getDbCollections;

@Configuration
@EnableArangoRepositories(basePackages = {"com.ruczajsoftware.workoutrival"})
public class ArangoDBConfig implements ArangoConfiguration {

    private static final String DB_NAME = "workout-rival-db";

    @Value("${arangodb.host}")
    private String host;

    @Value("${arangodb.port}")
    private Integer port;


    @Bean
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host(host, port)
                .user("root")
                .password("root");
    }

    @Bean
    public String database() {
        return DB_NAME;
    }

    @PostConstruct
    public void initDatabase() {
        ArangoDB arangoDB = arango().build();
        if (!arangoDB.getDatabases().contains(DB_NAME)) {
            arangoDB.createDatabase(DB_NAME);
        }
        ArangoDatabase database = arangoDB.db(DB_NAME);
        initCollections(database);
    }

    private void initCollections(ArangoDatabase database) {
        final List<String> dbCollectionsNames = database.getCollections().stream()
                .map(CollectionEntity::getName)
                .collect(Collectors.toList());

        getDbCollections().forEach(
                collectionName -> {
                    if (!dbCollectionsNames.contains(collectionName)) {
                        database.createCollection(collectionName);
                    }
                }
        );
    }
}
