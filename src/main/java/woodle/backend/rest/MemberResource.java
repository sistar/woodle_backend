package woodle.backend.rest;

import woodle.backend.model.Appointment;
import woodle.backend.model.Member;

import javax.ws.rs.*;
import java.util.List;

import static woodle.backend.rest.MemberResourceService.APPLICATION_JSON;

@Path("/members")
public interface MemberResource {
    @POST //TODO I think this is idempotent and should therefor be put
    @Path("/{email}")
    @Consumes(APPLICATION_JSON)
    public void modifyMember(Member member, @PathParam("email") String email);

    @PUT
    @Path("add")
    @Consumes(APPLICATION_JSON)
    public void addMember(Member member);

    /**
     * TODO required by MarenS, but probably obsolete, as members have to authorize with email and password anyway
     *
     * @param email
     * @param password
     */
    @POST
    @Path("authorisation")
    @Consumes(APPLICATION_JSON)
    public void login(String email, String password);

    @GET
    @Produces(APPLICATION_JSON)
    public List<Member> listAllMembers();

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberById(@PathParam("id") long id);

    @GET
    @Path("/{email}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberByEmail(@PathParam("email") String email);

    @GET
    @Path("/{email}/appointments")
    @Produces(APPLICATION_JSON)
    public List<Appointment> lookupAppointmentsForMemberEMail(@PathParam("email") String eMail);


}

