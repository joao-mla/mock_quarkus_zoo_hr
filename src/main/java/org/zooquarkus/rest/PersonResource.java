package org.zooquarkus.rest;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.zooquarkus.persistence.entities.Person;
import org.zooquarkus.persistence.entities.User;
import org.zooquarkus.services.CodecPersonService;
import org.zooquarkus.services.PersonServicePanache;

import java.util.List;

@Path("/v1/people")
@RequiredArgsConstructor
public class PersonResource {

    private final CodecPersonService codecPersonService;
    private final PersonServicePanache personServicePanache;

    @GET
    public List<Person> list() {
        return codecPersonService.list();
    }

    @GET
    @Path("reactive")
    public Uni<List<Person>> listReactive() {
        return codecPersonService.reactiveList();
    }

    @GET
    @Path("panache")
    public Uni<List<Person>> listPanache() {
        return personServicePanache.list();
    }

    @POST
    public void create(Person person) {
        codecPersonService.add(person);
    }

    @POST
    @Path("reactive")
    public Uni<Void> createReactive(Person person) {
        return codecPersonService.addReactive(person);
    }

    @POST
    @Path("panache")
    public Uni<Void> createPanache(Person person) {
        return personServicePanache.create(person);
    }


    @GET
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    @Path("public")
    public String publicResource() {
        return "public";
    }

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    @Path("admin")
    public String adminResource() {
        return "admin";
    }

    @GET
    @RolesAllowed("user")
    @Path("/me")
    public String me(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }
}
