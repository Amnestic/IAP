import db.Database;
import db.DatabaseMock;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import resources.AuthorizationServerResource;
import resources.ClientResource;
import resources.ResourceServerResource;

public class Aflevering2Application extends Application<Aflevering2Configuration> {
    public static void main(String[] args) throws Exception {
        new Aflevering2Application().run(args);
    }

    @Override
    public void initialize(Bootstrap<Aflevering2Configuration> bootstrap) {
        // nothing to do yet
    }


    public void run(Aflevering2Configuration configuration, Environment environment) throws Exception {
        Database database = new DatabaseMock();

        final AuthorizationServerResource authorizationServerResource = new AuthorizationServerResource(database);
        final ResourceServerResource resourceServerResource = new ResourceServerResource();
        final ClientResource clientResource = new ClientResource(1337);
        environment.jersey().register(authorizationServerResource);
        environment.jersey().register(resourceServerResource);
        environment.jersey().register(clientResource);
    }
}
