import db.Database;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import resources.SecurityKeyResource;

public class Aflevering3Application extends Application<Aflevering3Configuration> {
    public static void main(String[] args) throws Exception {
        new Aflevering3Application().run(args);
    }

    @Override
    public void initialize(Bootstrap<Aflevering3Configuration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }


    public void run(Aflevering3Configuration configuration, Environment environment) throws Exception {
        Database database = new Database();
        String host = (configuration.getProduction()) ? configuration.getProductionhost() : configuration.getLocalhost();
        final SecurityKeyResource securityKeyResource = new SecurityKeyResource(host, database);
        environment.jersey().register(securityKeyResource);
    }
}
