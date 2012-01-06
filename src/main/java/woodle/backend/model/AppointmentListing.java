package woodle.backend.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "appointments")
public class AppointmentListing {
    private List<Appointment> appointments;

    public AppointmentListing() {
    }

    public AppointmentListing(Set<Appointment> appointments) {
        this.appointments = new ArrayList<Appointment>();
        this.appointments.addAll(appointments);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
