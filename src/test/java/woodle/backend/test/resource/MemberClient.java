package woodle.backend.test.resource;

import woodle.backend.model.AppointmentListing;
import woodle.backend.model.Member;

import javax.ws.rs.*;
import java.util.List;

@Path("/members")
public interface MemberClient {
    @PUT
    @Path("/{email}")
    @Consumes("application/json")
    public void modifyMember(Member member);

    @POST
    @Consumes("application/json")
    public void addMember(Member member);

    @GET
    @Produces("text/xml")
    public List<Member> listAllMembers();

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("text/xml")
    public Member lookupMemberById(@PathParam("id") long id);

    @GET
    @Path("/{email}/appointments")
    @Produces("application/json")
    public AppointmentListing lookupAppointmentsForMemberEMail(@PathParam("email") String eMail);

}

