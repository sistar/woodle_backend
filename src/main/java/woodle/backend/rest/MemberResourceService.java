package woodle.backend.rest;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Member;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;


@RequestScoped
public class MemberResourceService implements MemberResource {

    public static final String APPLICATION_JSON = "application/json";

    @Context
    SecurityContext context;

    @Inject
    private WoodleStore woodleStore;

    @Inject
    Logger logger;

    @Override
    public void modifyMember(Member member, @PathParam("email") String email) {
        String userEmail = context.getUserPrincipal().getName();
        logger.info("PRINCIPAL EMAIL:" + email);
        Member oldMember = woodleStore.getMember(email);
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

    @Override
    public List<Appointment> lookupAppointmentsAttendance(@PathParam("email") String eMail) {
        return woodleStore.appointmentsAttendance(eMail);
    }

    @Override
    public List<Appointment> lookupAppointmentsAttendanceWaiting(@PathParam("email") String eMail) {
        return woodleStore.appointmentsAttendanceWaiting(eMail);
    }

    @Override
    public List<Appointment> lookupAppointmentsAttendanceConfirmed(@PathParam("email") String eMail) {
        return woodleStore.appointmentsAttendanceConfirmed(eMail);
    }


}
