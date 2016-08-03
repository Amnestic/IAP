import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class Aflevering3Configuration extends Configuration {
    private boolean production;
    private String localhost;
    private String productionhost;

    @JsonProperty
    public void setProduction(boolean production) { this.production = production; }

    @JsonProperty
    public boolean getProduction() { return production; }

    @JsonProperty
    public String getProductionhost() {
        return productionhost;
    }

    @JsonProperty
    public void setProductionhost(String productionhost) {
        this.productionhost = productionhost;
    }

    @JsonProperty
    public String getLocalhost() {
        return localhost;
    }

    @JsonProperty
    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }
}
