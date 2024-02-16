package events;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;
import org.zooquarkus.persistence.entities.User;

@ApplicationScoped
@Slf4j
public class Startup {
    public void onStart(@Observes StartupEvent evt) throws Throwable {
        // reset and load all test users
        VertxContextSupport.subscribeAndAwait(() -> Panache.withTransaction(() -> {
            User admin = User.create("admin", "admin", "admin");
            User user = User.create("user", "user", "user");

            return User.deleteAll()
                    .invoke(l -> log.info("deleted [{}] users", l))
                    .chain(d -> User.persist(admin, user))
                    .chain(v -> User.findAll().list())
                    .invoke(all -> log.info("Created [{}] users", all.size()));
        }));

    }
}