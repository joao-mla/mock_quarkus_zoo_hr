package org.zooquarkus.persistence.entities.codecs;

import com.mongodb.MongoClientSettings;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.zooquarkus.persistence.entities.Person;

import java.util.UUID;

public class PersonCodec implements CollectibleCodec<Person> {
    private final Codec<Document> documentCodec;

    public PersonCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Person generateIdIfAbsentFromDocument(Person person) {
        if (!documentHasId(person))
            person.setId(UUID.randomUUID());
        return person;
    }

    @Override
    public boolean documentHasId(Person person) {
        return person.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Person person) {
        return new BsonString(person.getId().toString());
    }

    @Override
    public Person decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);
        Person person = new Person();
        if (document.getString("id") != null)
            person.setId(UUID.fromString(document.getString("id")));
        person.setForename(document.getString("forename"));
        person.setSurname(document.getString("surname"));
        return person;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Person person, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put("forename", person.getForename());
        doc.put("surname", person.getSurname());
        documentCodec.encode(bsonWriter, doc, encoderContext);
    }

    @Override
    public Class<Person> getEncoderClass() {
        return Person.class;
    }
}
