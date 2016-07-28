package resources;

import api.Saying;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/resource")
@Produces(MediaType.APPLICATION_JSON)
public class ResourceServerResource {

    @GET
    public Saying wutFace() {
        return new Saying(10, "hej");
    }
}
