package org.zooquarkus.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.zooquarkus.persistence.entities.Person;
import org.zooquarkus.persistence.entities.Zookeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class ZookeeperService {
    private final ReactiveMongoClient reactiveMongoClient;
    private final MongoClient mongoClient;

    private static final String QUARKUS_DB = "quarkus_db";
    private ReactiveMongoCollection<Document> reactiveCollection;
    private MongoCollection<Document> collection;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        reactiveCollection = reactiveMongoClient.getDatabase(QUARKUS_DB).getCollection("zookeeper");
        collection = mongoClient.getDatabase(QUARKUS_DB).getCollection("zookeeper");
    }

    public Uni<List<Zookeeper>> list() {
        return reactiveCollection.find()
                .map(doc -> {
                    Zookeeper zookeeper = new Zookeeper();
                    zookeeper.setEnclosures(convertToSet(doc.getString("enclosures")));
                    return zookeeper;
                })
                .collect()
                .asList();
    }


    public List<Zookeeper> listAllZookeepers() {
        List<Zookeeper> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Zookeeper zookeeper = new Zookeeper();
                zookeeper.setEnclosures(convertToSet(document.getString("enclosures")));
                zookeeper.setPerson(objectMapper.readValue(document.getString("person"), Person.class));
                log.info(document.getString("enclosures"));
                log.info(document.get("person").toString());
                list.add(zookeeper);
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void add(Zookeeper zookeeper) {
        Document document = new Document()
                .append("person", zookeeper.getPerson())
                .append("enclosures", zookeeper.getEnclosures().toString());
        collection.insertOne(document);
    }

    public Uni<Void> addReactive(Zookeeper zookeeper) {
        Document document = new Document()
                .append("person", zookeeper.getPerson())
                .append("enclosures", zookeeper.getEnclosures());
        return reactiveCollection.insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }

    private Set<UUID> convertToSet(String enclosures) {
        return Arrays.stream(enclosures.replace("[", "")
                .replace("]", "")
                .replaceAll("\\s", "")
                .split(","))
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }
}
