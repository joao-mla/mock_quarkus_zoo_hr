package org.zooquarkus.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.zooquarkus.persistence.entities.Person;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class CodecPersonService {

    private static final String DB_NAME = "quarkus_db";
    private static final String COLLECTION_NAME = "people";

    private final MongoClient mongoClient;
    private final ReactiveMongoClient reactiveMongoClient;

    private MongoCollection<Person> collection;
    private ReactiveMongoCollection<Person> reactiveCollection;

    @PostConstruct
    private void init() {
        collection = mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME, Person.class);
        reactiveCollection = reactiveMongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME, Person.class);
    }

    public List<Person> list() {
        List<Person> list = new ArrayList<>();

        try (MongoCursor<Person> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }

        return list;
    }

    public Uni<List<Person>> reactiveList() {
        return reactiveCollection.find()
                .collect()
                .asList();
    }

    public void add(Person person) {
        collection.insertOne(person);
    }

    public Uni<Void> addReactive(Person person) {
        return reactiveCollection.insertOne(person)
                .onItem()
                .ignore()
                .andContinueWithNull();
    }
}
