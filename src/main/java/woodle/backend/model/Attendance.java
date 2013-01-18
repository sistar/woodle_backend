package woodle.backend.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Embeddable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Embeddable
public class Attendance implements Comparable<Attendance> {
    String attendantEmail;
    String calendarEventId;
    private Long timeOfEntry;

    public Attendance() {
        this.timeOfEntry = System.currentTimeMillis();
    }

    public Attendance(Attendance attendance) {
        this.attendantEmail = attendance.getAttendantEmail();
        this.calendarEventId = attendance.getCalendarEventId();
        this.timeOfEntry = System.currentTimeMillis();
    }

    public Attendance(String attendantEmail, String calendarEventId) {
        this.attendantEmail = attendantEmail;
        this.calendarEventId = calendarEventId;
        this.timeOfEntry = System.currentTimeMillis();

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

    public Long getTimeOfEntry() {
        return this.timeOfEntry;
    }

    public void setTimeOfEntry(Long timeOfEntry) {

        this.timeOfEntry = timeOfEntry;
    }

    @Override
    public int compareTo(Attendance o) {
        if (!(this.timeOfEntry == null || o.timeOfEntry == null)) {
            int timeCompared = this.timeOfEntry.compareTo(o.getTimeOfEntry());
            if (timeCompared != 0) {
                return timeCompared;
            }
        }
        if (this.attendantEmail != null && o.getAttendantEmail() != null) {
            return attendantEmail.compareTo(o.getAttendantEmail());
        } else {
            return 1;
        }
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
        if (timeOfEntry != null ? !timeOfEntry.equals(that.timeOfEntry) : that.timeOfEntry != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attendantEmail != null ? attendantEmail.hashCode() : 0;
        result = 31 * result + (calendarEventId != null ? calendarEventId.hashCode() : 0);
        result = 31 * result + (timeOfEntry != null ? timeOfEntry.hashCode() : 0);
        return result;
    }
}

