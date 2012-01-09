package woodle.backend.model;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AppointmentListing {
    private List<Appointment> appointments;

    public AppointmentListing() {
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public AppointmentListing(Set<Appointment> appointments) {
        this.appointments = new ArrayList<Appointment>();
        this.appointments.addAll(appointments);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
