package woodle.backend.rest;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.Member;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/members")
@RequestScoped
public class MemberResourceRESTService {
    public static final String APPLICATION_JSON = "application/json";
    @Inject
    private WoodleStore woodleStore;


    @PUT
    @Path("/{email}")
    @Consumes(APPLICATION_JSON)
    public void modifyMember(Member member, @PathParam("email") String email) {
        Member oldMember = woodleStore.getMember(email);
        woodleStore.saveMember(member);
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public void addMember(Member member) {

        if(member.getEmail() == null){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (woodleStore.hasMember(member.getEmail())) {
            throw new WebApplicationException(Response.Status.CONFLICT); //TODO read Rest in Practice Chapter 4. CRUD Web Services
        }
        woodleStore.saveMember(member);
    }


    @GET
    @Produces(APPLICATION_JSON)
    public Collection<Member> listAllMembers() {
        //@SuppressWarnings("unchecked")
        // We recommend centralizing inline queries such as this one into @NamedQuery annotations on
        // the @Entity class
        // as described in the named query blueprint:
        // https://blueprints.dev.java.net/bpcatalog/ee5/persistence/namedquery.html
        //final List<Member> results = em.createQuery("select m from Member m order by m.name").getResultList();
        final Collection<Member> results = woodleStore.getMembers();
        return results;
    }

    @GET
    @Path("/{email}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberByEmail(@PathParam("email") String email) {
        //return em.find(Member.class, id);
        return woodleStore.getMember(email);
    }

    @GET
    @Path("/{email}/appointments")
    @Produces(APPLICATION_JSON)
    public AppointmentListing lookupAppointmentsForMemberEMail(@PathParam("email") String eMail) {
        AppointmentListing ret = woodleStore.appointmentsForUser(eMail);
        System.out.println("returning appointment listing containing " + ret.getAppointments().size() + " appointments");
        //return em.find(Member.class, id);
        return ret;
    }



}
