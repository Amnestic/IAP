import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class Aflevering3aApplication extends Application<Aflevering3aConfiguration> {
    public static void main(String[] args) throws Exception {
        new Aflevering3aApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<Aflevering3aConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }


    public void run(Aflevering3aConfiguration configuration, Environment environment) throws Exception {

        String host = (configuration.getProduction()) ? "test" : "gest";
        //final SecretKeyResource secretKeyResource = new SecretKeyResource(host);
        //environment.jersey().register(secretKeyResource);
    }
}
