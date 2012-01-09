package woodle.backend.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.NoJackson;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.Member;
import woodle.backend.model.MemberWrapper;

@Path("/members")
@RequestScoped
public class MemberResourceRESTService {
    public static final String APPLICATION_JSON = "application/json";
    @Inject
    private WoodleStore woodleStore;


    @PUT
    @Path("/{email}")
    @Consumes(APPLICATION_JSON)
    public void modifyMember(Member member) {

    }

    @POST
    @Consumes(APPLICATION_JSON)
    public void addMember(Member member) {
        //Member member = memberWrapper.getMember();

        if (woodleStore.hasMember(member.getEmail())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST); //TODO read Rest in Practice Chapter 4. CRUD Web Services
        }
        woodleStore.saveMember(member);
    }


    @GET
    @Produces(APPLICATION_JSON)
    public List<Member> listAllMembers() {
        // Use @SupressWarnings to force IDE to ignore warnings about "genericizing" the results of
        // this query
        //@SuppressWarnings("unchecked")
        // We recommend centralizing inline queries such as this one into @NamedQuery annotations on
        // the @Entity class
        // as described in the named query blueprint:
        // https://blueprints.dev.java.net/bpcatalog/ee5/persistence/namedquery.html
        //final List<Member> results = em.createQuery("select m from Member m order by m.name").getResultList();
        return null;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(APPLICATION_JSON)
    public Member lookupMemberById(@PathParam("id") long id) {
        //return em.find(Member.class, id);
        return null;
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
