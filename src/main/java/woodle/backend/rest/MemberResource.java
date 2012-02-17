package woodle.backend.rest;

import woodle.backend.model.Appointment;
import woodle.backend.model.Member;

import javax.ws.rs.*;
import java.util.List;

import static woodle.backend.rest.MemberResourceService.APPLICATION_JSON;

@Path("/members")
public interface MemberResource {

    @PUT
    @Path("/{email}")
    @Consumes(APPLICATION_JSON)
    public void modifyMember(Member member, @PathParam("email") String email);

    @GET
    @Produces(APPLICATION_JSON)
    public List<Member> listAllMembers();

    @GET
    @Path("/{email}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberByEmail(@PathParam("email") String email);

    @GET
    @Path("/{email}/appointments")
    @Produces(APPLICATION_JSON)
    public List<Appointment> lookupAppointmentsForMemberEMail(@PathParam("email") String eMail);

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberById(@PathParam("id") long id);

}

