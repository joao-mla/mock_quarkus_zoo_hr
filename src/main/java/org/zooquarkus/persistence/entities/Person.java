package org.zooquarkus.persistence.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.UUID;

// Active record pattern
@EqualsAndHashCode(callSuper = true)
@Data
@MongoEntity(collection = "people")
public class Person extends PanacheMongoEntity {
    private UUID id;
    private String forename;
    private String surname;
    @BsonProperty("birth")
    private LocalDate birthDate;
}
