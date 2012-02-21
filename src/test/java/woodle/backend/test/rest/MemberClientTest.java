package woodle.backend.test.rest;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.controller.MemberRegistration;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Member;
import woodle.backend.rest.AppointmentResource;
import woodle.backend.rest.ManagementResource;
import woodle.backend.rest.MemberResource;
import woodle.backend.rest.RegisterResource;
import woodle.backend.util.Resources;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class MemberClientTest extends RestClientTest {

    public static final String NOT_SO_SECRET = "notSoSecret";

    @Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("woodle.backend.model")
                .addPackage("woodle.backend.rest")
                .addClasses(MemberRegistration.class, WoodleStore.class
                )

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addClass(Resources.class)
                .merge(AUTHENTICATION);
    }

    @Test
    public void testCreateANewMember() {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        assertThat(client(MemberResource.class, SANTA_CLAUS_NO, "secret").listAllMembers().size(), is(equalTo(0)));
        createMember(client(RegisterResource.class, SANTA_CLAUS_NO, "secret"), SANTA_CLAUS_NO, "secret");
        assertThat(client(MemberResource.class, SANTA_CLAUS_NO, "secret").listAllMembers().size(), is(equalTo(1)));
    }

    @Test
    public void modifyAMember() {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        testCreateANewMember();

        client(MemberResource.class, SANTA_CLAUS_NO, "secret").modifyMember(new Member(NOT_SO_SECRET, SANTA_CLAUS_NO, "81955840"), SANTA_CLAUS_NO);

        assertThat(client(MemberResource.class, SANTA_CLAUS_NO, "secret").listAllMembers().size(), is(equalTo(1)));
        Member member = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupMemberByEmail(SANTA_CLAUS_NO);
        assertThat(member.getPassword(), is(equalTo(NOT_SO_SECRET)));
    }

    @Test
    public void testPutAppointmentUsingClientProxy() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        testCreateANewMember();
        createAppointment(client(AppointmentResource.class, SANTA_CLAUS_NO, "secret"), SANTA_CLAUS_NO, false);
        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        Appointment next = appointments.iterator().next();
        assertThat(next.getStartDate(), is(equalTo(APPOINTMENT_DATE)));
    }

    @Test
    public void testShowAllAppointmentsWhichIHaveCreated() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        createAppointment(SANTA_CLAUS_NO, "secret", false);
        createAppointment(MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret", false);
        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        assertThat(appointments.size(), is(equalTo(1)));
        assertThat(appointments.get(0).getUser(), is(equalTo(SANTA_CLAUS_NO)));
    }

    @Test
    public void testShowAllAppointmentsWhichIBelongTo() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        createAppointment(SANTA_CLAUS_NO, "secret", false);
        createAppointment(MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret", false);
        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsAttendance(SANTA_CLAUS_NO);
        assertThat(appointments.size(), is(equalTo(2)));
        appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsAttendanceWaiting(SANTA_CLAUS_NO);
        assertThat(appointments.size(), is(equalTo(0)));
        appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsAttendanceConfirmed(SANTA_CLAUS_NO);
        assertThat(appointments.size(), is(equalTo(2)));
        appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsAttendanceConfirmed("rupert@north.pole");
        assertThat(appointments.size(), is(equalTo(0)));
        appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsAttendanceWaiting("rupert@north.pole");
        assertThat(appointments.size(), is(equalTo(2)));
    }
}
