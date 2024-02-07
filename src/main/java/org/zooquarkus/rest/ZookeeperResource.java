package org.zooquarkus.rest;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.zooquarkus.rest.viewmodels.PersonViewModel;
import org.zooquarkus.rest.viewmodels.ZookeeperViewModel;

import java.util.UUID;

@Path("/v1/zookeepers/{id}")
@RegisterRestClient( configKey="hr_api_yaml")
public class ZookeeperResource {

    /**
     * Fetches a zookeeper responsible for an enclosure of a specific ID
     * @param id the ID of the enclosure
     * @return The zookeeper information
     */
    @GET
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
}