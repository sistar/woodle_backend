package woodle.backend.model;

public class AppointmentKey {
    private String title;
    private String start;
    private String creatorEmail;


    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public AppointmentKey(String title, String start, String creatorEmail
    ) {
        this.title = title;
        this.start = start;
        this.creatorEmail = creatorEmail;

    }

    public AppointmentKey(Appointment appointment) {
        this.title = appointment.getTitle();
        this.start = appointment.getStartDate();
        this.creatorEmail = appointment.getUser();

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentKey)) return false;

        AppointmentKey that = (AppointmentKey) o;

        if (creatorEmail != null ? !creatorEmail.equals(that.creatorEmail) : that.creatorEmail != null) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (creatorEmail != null ? creatorEmail.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "AppointmentKey{" +
                "title='" + title + '\'' +
                ", start=" + start +
                ", creatorEmail='" + creatorEmail + '\'' +
                '}';
    }

    /*
   @return a key formatted title-2012-03-21T16.20.00.000+01:00
    */
    public String getId() {
        return this.title + "-" + start;
    }
}
