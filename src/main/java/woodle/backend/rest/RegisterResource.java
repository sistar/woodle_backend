package woodle.backend.rest;

import woodle.backend.model.Member;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static woodle.backend.rest.MemberResourceService.APPLICATION_JSON;

@Path("/register")
public interface RegisterResource {

    @POST
    @Consumes(APPLICATION_JSON)
    public void createMember(Member member);

}

