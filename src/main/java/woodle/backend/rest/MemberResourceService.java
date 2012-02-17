package woodle.backend.rest;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Member;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RequestScoped
public class MemberResourceService implements MemberResource {
    public static final String APPLICATION_JSON = "application/json";

    @Inject
    private WoodleStore woodleStore;


    @Override
    public void modifyMember(Member member, @PathParam("email") String email) {
        Member oldMember = woodleStore.getMember(email);
        woodleStore.saveMember(member);
    }

    @Override
    public void createMember(Member member) {

        if (member.getEmail() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (woodleStore.hasMember(member.getEmail())) {
            throw new WebApplicationException(Response.Status.CONFLICT); //TODO read Rest in Practice Chapter 4. CRUD Web Services
        }
        woodleStore.saveMember(member);
    }

    @Override
    public List<Member> listAllMembers() {
        final Collection<Member> results = woodleStore.getMembers();
        return new ArrayList<Member>(results);
    }

    @Override
    public Member lookupMemberById(@PathParam("id") long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Member lookupMemberByEmail(@PathParam("email") String email) {
        return woodleStore.getMember(email);
    }

    @Override
    public List<Appointment> lookupAppointmentsForMemberEMail(@PathParam("email") String eMail) {
        return woodleStore.appointmentsForUser(eMail);
    }


}
