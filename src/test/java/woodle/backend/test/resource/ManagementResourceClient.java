package woodle.backend.test.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/management")
public interface ManagementResourceClient {

    @POST
    @Path("/resetmembers")
    void reset();
}
