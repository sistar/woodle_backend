package woodle.backend.test;


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
import woodle.backend.util.Resources;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class MemberClientOnlyTest extends RestClientTest {

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
    public void testPutAppointmentUsingClientProxy() throws Exception {


        managementResource.reset();
        createMember(memberClient);
        createAppointment(appointmentClient);
        List<Appointment> appointments = memberClient.lookupAppointmentsForMemberEMail(AppointmentClientTest.SANTA_CLAUS_NO);
        Appointment next = appointments.iterator().next();
        assertThat(next.getStartDate(), is(equalTo(APPOINTMENT_DATE)));

    }

}
