import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import resources.Aflevering2Resource;

public class Aflevering2Application extends Application<Aflevering2Configuration> {
    public static void main(String[] args) throws Exception {
        new Aflevering2Application().run(args);
    }

    @Override
    public void initialize(Bootstrap<Aflevering2Configuration> bootstrap) {
        // nothing to do yet
    }


    public void run(Aflevering2Configuration configuration, Environment environment) throws Exception {
        final Aflevering2Resource aflevering2Resource = new Aflevering2Resource(configuration.getTemplate(), configuration.getDefaultName());
        environment.jersey().register(aflevering2Resource);
    }
}
