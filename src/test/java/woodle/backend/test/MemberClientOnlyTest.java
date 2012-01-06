package woodle.backend.test;

import org.jboss.resteasy.client.ProxyFactory;
import org.junit.Test;
import woodle.backend.model.AppointmentListing;
import woodle.backend.test.resource.AppointmentClient;
import woodle.backend.test.resource.MemberClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class MemberClientOnlyTest {
    @Test
    public void testPutAppointmentUsingClientProxy() throws Exception {
        AppointmentClient appointmentClient = ProxyFactory.create(AppointmentClient.class,
                "http://localhost:8080/woodle.backend/rest");
        MemberClient memberClient = ProxyFactory.create(MemberClient.class,
                "http://localhost:8080/woodle.backend/rest");
        AppointmentClientTest.createMember(memberClient);
        AppointmentClientTest.createAppointment(appointmentClient);
        AppointmentListing appointments = memberClient.lookupAppointmentsForMemberEMail(AppointmentClientTest.SANTA_CLAUS_NO);
        assertThat(appointments.getAppointments().iterator().next().getStart(), is(equalTo(AppointmentClientTest.APPOINTMENT_DATE)));

    }
}
