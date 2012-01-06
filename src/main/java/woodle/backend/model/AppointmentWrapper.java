package woodle.backend.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class AppointmentWrapper {
    Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
