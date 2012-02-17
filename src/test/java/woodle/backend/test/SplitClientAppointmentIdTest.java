package woodle.backend.test;


import org.junit.Test;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentKey;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SplitClientAppointmentIdTest {
    public static final String SANTA_CLAUS_NO = "santa@claus.no";
    public static final String JSON_HACKING = "json Hacking-2020";
    public static final String APPOINTMENT_DATE = "2012-02-01T13:10:00.000+01:00";
    public static final String APPOINTMENT_ID = JSON_HACKING + "-" + APPOINTMENT_DATE;

    @Test
    public void testThatSimpleAppointmentIdIsSplitted() throws Exception {
        String[] splitResult = APPOINTMENT_ID.split(Appointment.DATE_MATCH);
        assertThat(splitResult[0], is(equalTo(JSON_HACKING)));
        assertThat(splitResult[1], is(equalTo(APPOINTMENT_DATE)));

    }

    @Test
    public void testThatAppointmentKeyIsCreatedFromClientAppointmentKey() throws Exception {
        AppointmentKey appointmentKey = Appointment.createAppointmentKey(SANTA_CLAUS_NO, APPOINTMENT_ID);
        assertThat(appointmentKey, is(equalTo(new AppointmentKey(JSON_HACKING, APPOINTMENT_DATE, SANTA_CLAUS_NO))));

    }


}
