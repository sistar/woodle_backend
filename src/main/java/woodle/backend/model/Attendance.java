package woodle.backend.model;


public class Attendance {

    String attendantEmail;
    String creatorEmail;
    String calendarEventId;

    public Attendance() {

    }


    public Attendance(String attendantEmail, String calendarEventId) {
        this.attendantEmail = attendantEmail;
        this.calendarEventId = calendarEventId;
    }

    public Attendance(String attendantEmail, String calendarEventId, String creatorEmail) {
        this.attendantEmail = attendantEmail;
        this.calendarEventId = calendarEventId;
        this.creatorEmail = creatorEmail;
    }

    public String getAttendantEmail() {
        return attendantEmail;
    }

    public void setAttendantEmail(String attendantEmail) {
        this.attendantEmail = attendantEmail;
    }

    public String getCalendarEventId() {
        return calendarEventId;
    }

    public void setCalendarEventId(String calendarEventId) {
        this.calendarEventId = calendarEventId;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }


    @Override
    public String toString() {
        return "Attendance{" +
                "attendantEmail='" + attendantEmail + '\'' +
                ", calendarEventId='" + calendarEventId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attendance)) return false;

        Attendance that = (Attendance) o;

        if (attendantEmail != null ? !attendantEmail.equals(that.attendantEmail) : that.attendantEmail != null)
            return false;
        if (calendarEventId != null ? !calendarEventId.equals(that.calendarEventId) : that.calendarEventId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attendantEmail != null ? attendantEmail.hashCode() : 0;
        result = 31 * result + (calendarEventId != null ? calendarEventId.hashCode() : 0);
        return result;
    }
}


