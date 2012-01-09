package woodle.backend.test;

import org.jboss.resteasy.client.ProxyFactory;
import org.junit.Ignore;
import org.junit.Test;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;
import woodle.backend.rest.ManagementResource;
import woodle.backend.test.resource.AppointmentClient;
import woodle.backend.test.resource.ManagementResourceClient;
import woodle.backend.test.resource.MemberClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class MemberClientOnlyTest {
    @Ignore
    @Test
    public void testPutAppointmentUsingClientProxy() throws Exception {
        AppointmentClient appointmentClient = ProxyFactory.create(AppointmentClient.class,
                "http://localhost:18080/woodle_backend/rest");
        MemberClient memberClient = ProxyFactory.create(MemberClient.class,
                "http://localhost:18080/woodle_backend/rest");
        ManagementResourceClient managementResourceClient = ProxyFactory.create(ManagementResourceClient.class,
                "http://localhost:18080/woodle_backend/rest");
        managementResourceClient.reset();
        AppointmentClientTest.createMember(memberClient);
        AppointmentClientTest.createAppointment(appointmentClient);
        AppointmentListing appointments = memberClient.lookupAppointmentsForMemberEMail(AppointmentClientTest.SANTA_CLAUS_NO);
        Appointment next = appointments.getAppointments().iterator().next();
        assertThat(next.getStart(), is(equalTo(AppointmentClientTest.APPOINTMENT_DATE)));

    }
}
