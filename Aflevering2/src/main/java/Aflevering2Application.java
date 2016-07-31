import db.AuthorizationDatabase;
import db.AuthorizationDatabaseMock;
import db.ResourceDatabase;
import db.ResourceDatabaseMock;
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
        AuthorizationDatabase authorizationDatabase = new AuthorizationDatabaseMock();
        ResourceDatabase resourceDatabase = new ResourceDatabaseMock();

        final AuthorizationServerResource authorizationServerResource = new AuthorizationServerResource(authorizationDatabase);
        final ResourceServerResource resourceServerResource = new ResourceServerResource(resourceDatabase);
        final ClientResource clientResource = new ClientResource(1337, 7331);
        environment.jersey().register(authorizationServerResource);
        environment.jersey().register(resourceServerResource);
        environment.jersey().register(clientResource);
    }
}
