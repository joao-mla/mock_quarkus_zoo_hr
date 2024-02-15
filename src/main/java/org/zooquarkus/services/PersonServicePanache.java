package org.zooquarkus.services;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.zooquarkus.persistence.entities.Person;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class PersonServicePanache {

    public Uni<List<Person>> list() {
        return Uni.createFrom().item(Person.listAll());
    }

    public Uni<Void> create(Person person) {
        person.persist();
        return Uni.createFrom().voidItem();
    }
}
