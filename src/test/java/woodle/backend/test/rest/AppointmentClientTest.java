package woodle.backend.test.rest;


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
@RunAsClient
public class AppointmentClientTest extends RestClientTest {


    public static final String MAREN_SOETEBIER_GOOGLEMAIL_COM = "maren.soetebier@googlemail.com";

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
        createAppointment(SANTA_CLAUS_NO, "secret");
        List<Appointment> appointments = client(MemberResource.class, SANTA_CLAUS_NO, "secret").lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        assertThat(appointments.iterator().next().getStartDate(), is(equalTo(APPOINTMENT_DATE)));
    }


    @Test
    public void testLookupAppointmentById() throws Exception {
        testCreateAppointment();
        Appointment appointment = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").lookupById(APPOINTMENT_ID);
        assertThat(appointment.getTitle(), is(equalTo(JSON_HACKING)));
    }

    @Test
    public void testListAppointments() throws Exception {
        createAppointment(SANTA_CLAUS_NO, "secret");
        createAppointment(MAREN_SOETEBIER_GOOGLEMAIL_COM, "secret");
        List<Appointment> appointments = client(AppointmentResource.class, SANTA_CLAUS_NO, "secret").clientGetAppointments();
        assertThat(appointments.size(), is(equalTo(2)));
    }

    private void createAppointment(String userEmail, String password) {
        client(ManagementResource.class, userEmail, password).reset();
        createMember(client(RegisterResource.class, userEmail, password), userEmail, password);
        createAppointment(client(AppointmentResource.class, userEmail, password));
    }
}