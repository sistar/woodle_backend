package woodle.backend.model;

public class UnkownAppointmentException extends Exception {
    private String appointmentId;

    @Override
    public String getMessage() {
        return "unknown appointment: " + appointmentId;
    }

    public UnkownAppointmentException(String appointmentId) {

        this.appointmentId = appointmentId;
    }
}
