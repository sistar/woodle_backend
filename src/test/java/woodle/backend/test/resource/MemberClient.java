package woodle.backend.test.resource;

import woodle.backend.model.AppointmentListing;
import woodle.backend.model.Member;
import static woodle.backend.rest.MemberResourceRESTService.APPLICATION_JSON;

import javax.ws.rs.*;
import java.util.List;

@Path("/members")
public interface MemberClient {
    @PUT
    @Path("/{email}")
    @Consumes(APPLICATION_JSON)
    public void modifyMember(Member member,@PathParam("email") String email);

    @POST
    @Consumes(APPLICATION_JSON)
    public void addMember(Member member);

    @GET
    @Produces(APPLICATION_JSON)
    public List<Member> listAllMembers();

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberById(@PathParam("id") long id);

    @GET
    @Path("/{email}/appointments")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public AppointmentListing lookupAppointmentsForMemberEMail(@PathParam("email") String eMail);


}

