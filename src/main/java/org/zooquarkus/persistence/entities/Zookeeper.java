package org.zooquarkus.persistence.entities;

import lombok.Data;

import java.util.Set;
import java.util.UUID;


@Data
public class Zookeeper {
    private UUID id;

    private Person person;

    private Set<UUID> enclosures;
}
