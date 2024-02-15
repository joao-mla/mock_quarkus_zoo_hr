package org.zooquarkus.persistence.entities.codecs;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.zooquarkus.persistence.entities.Zookeeper;

import java.lang.reflect.Type;
import java.util.List;

public class ZookeeperCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass.equals(Zookeeper.class))
            return (Codec<T>) new ZookeeperCodec();
        return null;
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
        if (clazz.equals(Zookeeper.class))
            return (Codec<T>) new ZookeeperCodec();
        return null;
    }
}
