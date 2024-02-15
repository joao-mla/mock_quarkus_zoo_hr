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
import org.zooquarkus.persistence.entities.Zookeeper;

import java.util.HashSet;
import java.util.UUID;

public class ZookeeperCodec implements CollectibleCodec<Zookeeper> {

    private final Codec<Document> documentCodec;

    public ZookeeperCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Zookeeper generateIdIfAbsentFromDocument(Zookeeper zookeeper) {
        if (!documentHasId(zookeeper)) {
            zookeeper.setId(UUID.randomUUID());
        }
        return zookeeper;
    }

    @Override
    public boolean documentHasId(Zookeeper zookeeper) {
        return zookeeper.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Zookeeper zookeeper) {
        return new BsonString(zookeeper.getId().toString());
    }

    @Override
    public Zookeeper decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);
        Zookeeper zookeeper = new Zookeeper();
        if (document.getString("id") != null)
            zookeeper.setId(UUID.fromString(document.getString("id")));
        zookeeper.setPerson(document.get("person", Person.class));
        zookeeper.setEnclosures(new HashSet<>(document.getList("enclosures", UUID.class)));
        return zookeeper;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Zookeeper zookeeper, EncoderContext encoderContext) {
        Document doc = new Document();
        doc.put("person", zookeeper.getPerson());
        doc.put("enclosures", zookeeper.getEnclosures());
        documentCodec.encode(bsonWriter, doc, encoderContext);
    }

    @Override
    public Class<Zookeeper> getEncoderClass() {
        return Zookeeper.class;
    }
}
