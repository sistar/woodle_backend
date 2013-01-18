package woodle.backend.test.rest;

import com.google.common.collect.Sets;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Attendance;
import woodle.backend.model.Member;
import woodle.backend.rest.AppointmentResource;
import woodle.backend.rest.MemberResource;
import woodle.backend.rest.RegisterResource;

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
                .addClasses(WoodleStore.class)
                .merge(COMMON())
                .merge(AUTHENTICATION())
                .merge(PERSISTENCE());
    }

    @Test
    public void testCreateAppointment() throws Exception {
        resetCreateDefaultUserAndAppointment();
        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret")
                .lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        assertThat(appointments.iterator().next().getStartDate(), is(equalTo(APPOINTMENT_DATE)));
    }

    @Test
    public void testCreateAppointmentWithoutTimeOfEntry() throws Exception {
        long beforeSubmission = System.currentTimeMillis();
        resetCreateDefaultUserAndAppointment();
        long afterSubmission = System.currentTimeMillis();

        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret")
                .lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        Appointment appointment = appointments.iterator().next();
        assertThat(appointment.getStartDate(), is(equalTo(APPOINTMENT_DATE)));
        Attendance attendance = appointment.getAttendances().iterator().next();
        Long timeOfEntry = attendance.getTimeOfEntry();
        assertThat(timeOfEntry, notNullValue());
        assertThat(timeOfEntry, is(greaterThan(beforeSubmission)));
        assertThat(timeOfEntry, is(lessThan(afterSubmission)));
        assertThat(attendance.getCalendarEventId(), is(notNullValue()));
    }

    @Test
    public void testThatISeeOnlyAppointmentsIAttend() throws Exception {

        resetCreateDefaultUser();

        HashSet<Attendance> attendances = Sets.newHashSet();
        HashSet<Attendance> waiting = Sets.newHashSet();

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
        for (Attendance attendance : appointments.get(0).getAttendances()) {
            assertThat(attendance.getCalendarEventId(), notNullValue());
        }

    }

    @Test
    public void testDeleteAppointment() throws Exception {
        resetCreateDefaultUserAndAppointment();
        Appointment appointment = soleAppointment();
        client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").deleteAppointment(appointment.getId(), appointment.getUser());
        assertNoAppointment();
    }

    @Test
    public void testAttendAppointment() throws Exception {
        resetCreateDefaultUserAndAppointment();
        Appointment appointment = soleAppointment();
        assertThat(appointment.getAttendances().size(), is(equalTo(1)));
        String attend = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret")
                .attend(SANTA_CLAUS_NO, appointment.getId(),
                        new Attendance(MAREN_SOETEBIER_GOOGLEMAIL_COM, "CAL54321"));
        assertThat(attend, is(equalTo("confirmed")));
        appointment = soleAppointment();
        assertThat(appointment.getAttendances().size(), is(equalTo(2)));
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
        String attend = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").cancel(soleAppointment().getId(), soleAppointment().getUser());
        assertThat(soleAppointment().getAttendances().size(), is(equalTo(2)));
        assertThat(attend, is(equalTo(RUPERT_NORTH_POLE)));
        client(RegisterResource.class).createMember(new Member("secret", RUPERT_NORTH_POLE, "007007007", "Rupert"));
        attend = client(AppointmentResource.class, RUPERT_NORTH_POLE, "secret").cancel(soleAppointment().getId(), soleAppointment().getUser());
        assertThat(soleAppointment().getAttendances().size(), is(equalTo(1)));
        assertThat(attend, is(equalTo("")));
    }

    @Test
    public void testLookupAppointmentById() throws Exception {
        resetCreateDefaultUserAndAppointment();
        Appointment appointment = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret")
                .lookupById(SANTA_CLAUS_NO, JSON_HACKING, APPOINTMENT_DATE);
        assertThat(appointment.getTitle(), is(equalTo(JSON_HACKING)));
    }

    @Test
    public void testListAppointments() throws Exception {
        resetCreateDefaultUser();
        createMemberAndAppointment(MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret", false);
        List<Appointment> appointments = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").clientGetAppointments();
        assertThat(appointments.size(), is(equalTo(1)));
        assertThat(appointments.get(0).getUser(), is(MAREN_SOETEBIER_GOOGLEMAIL_COM));
    }

}