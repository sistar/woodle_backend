package woodle.backend.rest;


import woodle.backend.data.WoodleStore;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/management")
public class ManagementResource {
    @Inject
    private WoodleStore woodleStore;

    @POST
    @Path("resetmembers")
    public void reset(){
        System.out.println("RESET");
        woodleStore.resetMembers();
    }
}
