package woodle.backend.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/management")
public interface ManagementResource {

    @POST
    @Path("/reset")
    public void reset();

}
