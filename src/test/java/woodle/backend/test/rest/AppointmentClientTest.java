package woodle.backend.test.rest;


import com.google.common.collect.Sets;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Attendance;
import woodle.backend.model.ComparableAttendance;
import woodle.backend.rest.AppointmentResource;
import woodle.backend.rest.ManagementResource;
import woodle.backend.rest.MemberResource;
import woodle.backend.rest.RegisterResource;
import woodle.backend.util.Resources;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;


@RunWith(Arquillian.class)
@RunAsClient
public class AppointmentClientTest extends RestClientTest {


    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "woodle_backend.war").
                addPackage("woodle.backend.model")
                .addPackage("woodle.backend.rest")
                .addClasses(WoodleStore.class
                )
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addClass(Resources.class)
                .merge(AUTHENTICATION);
    }

    @Test
    public void testCreateAppointment() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        createMemberAndAppointment(SANTA_CLAUS_NO, "secret", false);
        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret")
                .lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        assertThat(appointments.iterator().next().getStartDate(), is(equalTo(APPOINTMENT_DATE)));
    }

    @Test
    public void testCreateAppointmentWithoutTimeOfEntry() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        long beforeSubmission = System.currentTimeMillis();
        createMemberAndAppointment(SANTA_CLAUS_NO, "secret", false);
        long afterSubmission = System.currentTimeMillis();

        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret")
                .lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        Appointment appointment = appointments.iterator().next();
        assertThat(appointment.getStartDate(), is(equalTo(APPOINTMENT_DATE)));
        Long timeOfEntry = appointment.getAttendances().iterator().next().getTimeOfEntry();
        assertThat(timeOfEntry, notNullValue());
        assertThat(timeOfEntry, is(greaterThan(beforeSubmission)));
        assertThat(timeOfEntry, is(lessThan(afterSubmission)));

    }

    @Test
    public void testThatISeeOnlyAppointmentsIAttend() throws Exception {

        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        createMember(client(RegisterResource.class, SANTA_CLAUS_NO, "secret"), SANTA_CLAUS_NO, "secret");


        HashSet<ComparableAttendance> attendances = Sets.newHashSet();
        HashSet<ComparableAttendance> waiting = Sets.newHashSet();

        Appointment appointmentIDoNotAttend = new Appointment(
                APPOINTMENT_ID,
                JSON_HACKING,
                "North Pole",
                "icy JSON stuff",
                APPOINTMENT_DATE,
                APPOINTMENT_DATE,
                attendances,
                waiting,
                SANTA_CLAUS_NO, 2);


        AppointmentResource appointmentResource = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret");
        appointmentResource.create(appointmentIDoNotAttend);


        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret")
                .lookupAppointmentsAttendance(SANTA_CLAUS_NO);
        assertThat(appointments.size(), is(equalTo(0)));

        Appointment appointment = appointmentResource.lookupById(SANTA_CLAUS_NO, JSON_HACKING, APPOINTMENT_DATE);
        appointmentResource.attend(SANTA_CLAUS_NO, JSON_HACKING, APPOINTMENT_DATE, "123456");

        appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret")
                .lookupAppointmentsAttendance(SANTA_CLAUS_NO);
        assertThat(appointments.size(), is(1));


    }


    @Test
    public void testDeleteAppointment() throws Exception {
        testCreateAppointment();
        Appointment appointment = soleAppointment();
        client(AppointmentResource.class, MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret").deleteAppointment(appointment.getId(), appointment.getUser());
        assertNoAppointment();
    }

    @Test
    public void testAttendAppointment() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        createMemberAndAppointment(SANTA_CLAUS_NO, "secret", false);
        Appointment appointment = soleAppointment();

        String attend = client(AppointmentResource.class, MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret")
                .attend(SANTA_CLAUS_NO, appointment.getId(),
                        new Attendance(MAREN_SOETEBIER_GOOGLEMAIL_COM, "CAL54321"));
        assertThat(attend, is(equalTo("confirmed")));
    }

    private Appointment soleAppointment() {
        List<Appointment> appointments = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret")
                .clientGetAppointments();
        assertThat(appointments.size(), is(equalTo(1)));
        return appointments.get(0);
    }

    private void assertNoAppointment() {
        List<Appointment> appointments = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret")
                .clientGetAppointments();
        assertThat(appointments.size(), is(equalTo(0)));
    }

    @Test
    public void testCancelAttendance() throws Exception {
        testAttendAppointment();

        assertThat(soleAppointment().getAttendances().size(), is(equalTo(2)));
        String attend = client(AppointmentResource.class, MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret").cancel(soleAppointment().getId(), soleAppointment().getUser());
        assertThat(soleAppointment().getAttendances().size(), is(equalTo(2)));
        assertThat(attend, is(equalTo("rupert@north.pole")));
        attend = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").cancel(soleAppointment().getId(), soleAppointment().getUser());
        assertThat(soleAppointment().getAttendances().size(), is(equalTo(1)));
        assertThat(attend, is(equalTo("")));
    }


    @Test
    public void testLookupAppointmentById() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        testCreateAppointment();
        Appointment appointment = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret")
                .lookupById(SANTA_CLAUS_NO, JSON_HACKING, APPOINTMENT_DATE);
        assertThat(appointment.getTitle(), is(equalTo(JSON_HACKING)));
    }

    @Test
    public void testListAppointments() throws Exception {
        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();
        createMemberAndAppointment(SANTA_CLAUS_NO, "secret", false);
        createMemberAndAppointment(MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret", false);
        List<Appointment> appointments = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").clientGetAppointments();
        assertThat(appointments.size(), is(equalTo(2)));
        assertThat(appointments.get(1).getAttendances().iterator().next().getCalendarEventId(), is(equalTo("CAL1234")));
        assertThat(appointments.get(0).getUser(), is(SANTA_CLAUS_NO));
        assertThat(appointments.get(1).getUser(), is(MAREN_SOETEBIER_GOOGLEMAIL_COM));

    }

}