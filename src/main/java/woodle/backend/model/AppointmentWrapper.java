package woodle.backend.model;

public class AppointmentWrapper {
    Appointment appointment;

    public AppointmentWrapper() {
    }

    public AppointmentWrapper(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
