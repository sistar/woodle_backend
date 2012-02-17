package woodle.backend.model;


public class ComparableAttendance extends Attendance implements Comparable<ComparableAttendance> {
    private Long timeOfEntry;

    public ComparableAttendance() {
    }

    public ComparableAttendance(Attendance attendance) {
        this.attendantEmail = attendance.getAttendantEmail();
        this.calendarEventId = attendance.getCalendarEventId();
        this.timeOfEntry = System.currentTimeMillis();
    }

    public ComparableAttendance(String attendantEmail, String calendarEventId) {
        this.attendantEmail = attendantEmail;
        this.calendarEventId = calendarEventId;
        this.timeOfEntry = System.currentTimeMillis();

    }

    public void setTimeOfEntry(long timeOfEntry) {

        this.timeOfEntry = timeOfEntry;
    }

    public long getTimeOfEntry() {
        return timeOfEntry;
    }

    @Override
    public int compareTo(ComparableAttendance o) {
        int timeCompared = this.timeOfEntry.compareTo(o.getTimeOfEntry());
        if (timeCompared != 0) {
            return timeCompared;
        }
        return attendantEmail.compareTo(o.getAttendantEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparableAttendance)) return false;

        ComparableAttendance that = (ComparableAttendance) o;

        if (timeOfEntry != null ? !timeOfEntry.equals(that.timeOfEntry) : that.timeOfEntry != null) return false;

        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int ret = timeOfEntry != null ? timeOfEntry.hashCode() : 0;
        ret = ret + super.hashCode();
        return ret;
    }
}

