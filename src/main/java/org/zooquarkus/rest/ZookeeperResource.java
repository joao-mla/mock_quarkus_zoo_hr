package org.zooquarkus.rest;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.zooquarkus.persistence.entities.Zookeeper;
import org.zooquarkus.rest.viewmodels.PersonViewModel;
import org.zooquarkus.rest.viewmodels.ZookeeperViewModel;
import org.zooquarkus.services.ZookeeperService;

import java.util.List;
import java.util.UUID;

@Path("/v1/zookeepers")
@RegisterRestClient( configKey="hr_api_yaml")
@RequiredArgsConstructor
public class ZookeeperResource {

    private final ZookeeperService zookeeperService;

    /**
     * Fetches a zookeeper responsible for an enclosure of a specific ID
     * @param id the ID of the enclosure
     * @return The zookeeper information
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ZookeeperViewModel> getSingleZookeeper(UUID id) {
        // Mock code
        ZookeeperViewModel vm = new ZookeeperViewModel();
        vm.setId(UUID.randomUUID());
        PersonViewModel personViewModel = new PersonViewModel();
        personViewModel.setId(UUID.randomUUID());
        personViewModel.setForename("John");
        personViewModel.setSurname("Doe");
        vm.setPerson(personViewModel);
        return Uni.createFrom().item(vm);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Zookeeper>> getAllZookeepers() {
        return Uni.createFrom().item(zookeeperService.listAllZookeepers());
    }

    @GET
    @Path("all-reactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Zookeeper>> getAllZookeepersReactive() {
        return zookeeperService.list();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Void> createZookeeper(Zookeeper zookeeper) {
        zookeeperService.add(zookeeper);
        return Uni.createFrom().voidItem();
    }

    @POST
    @Path("all-reactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Void> createZookeeperReactive(Zookeeper zookeeper) {
        zookeeperService.add(zookeeper);
        return Uni.createFrom().voidItem();
    }
}