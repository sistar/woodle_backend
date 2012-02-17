package woodle.backend.test;


import org.junit.Test;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.ComparableAttendance;

import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataTest {
    @Test
    public void testAddAppointment() throws Exception {
        WoodleStore woodleStore = new WoodleStore();
        Appointment appointment = new Appointment("theId", "someTitle", "location", "description", "2012-02-10", "2012-02-10",
                new TreeSet<ComparableAttendance>(), new TreeSet<ComparableAttendance>(), "user", 1);

        woodleStore.getAppointmentMap().put(appointment.createAppointmentKey(), appointment);

        assertThat(woodleStore.getAppointmentMap().size(), is(equalTo(1)));
        assertThat(woodleStore.appointmentsForUser("user").size(), is(equalTo(1)));

    }
}
