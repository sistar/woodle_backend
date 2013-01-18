package woodle.backend.model;

import com.google.common.collect.Iterables;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

@Entity
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Appointment {
    public static String DATE_MATCH = "-(?=\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}.*)";
    @Id
    private String id;
    private String title;
    private String location;
    private String description;
    private String startDate;
    private String endDate;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "ATTD",
            joinColumns = @JoinColumn(name = "APPOINTMENT_ID")
    )

    private Set<Attendance> attendances = new HashSet<Attendance>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "MAYBE_ATTD",
            joinColumns = @JoinColumn(name = "APPOINTMENT_ID")
    )
    private Set<Attendance> maybeAttendances = new HashSet<Attendance>();
    private String user;
    private int maxNumber;

    public Appointment() {
    }

    public Appointment(String id, String title, String location, String description,
                       String startDate, String endDate,
                       Set<Attendance> attendances, Set<Attendance> maybeAttendances, String user, int maxNumber) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        if (attendances != null) {
            this.attendances.addAll(attendances);
        }
        if (maybeAttendances != null) {
            this.maybeAttendances = maybeAttendances;
        }
        this.user = user;
        this.maxNumber = maxNumber;
    }

    public static AppointmentKey createAppointmentKey(String creatorEmail, String title, String startDate) {
        return new AppointmentKey(title, startDate, creatorEmail);
    }

    public static AppointmentKey createAppointmentKey(String creatorEmail, String appointmentId) {
        String[] splitResult = appointmentId.split(DATE_MATCH);
        return new AppointmentKey(splitResult[0], splitResult[1], creatorEmail);
    }

    public AppointmentKey createAppointmentKey() {
        return new AppointmentKey(this.getTitle(), this.getStartDate(), this.getUser());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(SortedSet<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Set<Attendance> getMaybeAttendances() {
        return maybeAttendances;
    }

    public void setMaybeAttendances(SortedSet<Attendance> maybeAttendances) {
        this.maybeAttendances = maybeAttendances;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", attendances=" + attendances +
                ", maybeAttendances=" + maybeAttendances +
                ", user='" + user + '\'' +
                ", maxNumber=" + maxNumber +
                '}';
    }

    public String attend(Attendance attendance) {
        if (hasMember(attendance.getAttendantEmail(), this.maybeAttendances)) return "waiting";
        if (hasMember(attendance.getAttendantEmail(), this.attendances)) return "confirmed";

        if (this.attendances.size() < maxNumber) {
            this.attendances.add(attendance);
            return "confirmed";
        } else {
            this.maybeAttendances.add(attendance);
            return "waiting";
        }
    }

    private boolean hasMember(String attendantEmail, Set<Attendance> Attendances) {
        for (Attendance attendance : Attendances) {
            if (attendance.getAttendantEmail().equals(attendantEmail)) return true;
        }
        return false;
    }

    public String cancel(String userEmail) {
        Iterables.removeIf(this.maybeAttendances, new WithEmail(userEmail));
        Iterables.removeIf(this.attendances, new WithEmail(userEmail));
        if (this.attendances.size() < maxNumber && this.maybeAttendances.size() > 0) {
            Attendance first = ((SortedSet<Attendance>) this.maybeAttendances).first();
            this.maybeAttendances.remove(first);
            this.attendances.add(first);
            return first.getAttendantEmail();
        } else {
            return "";
        }
    }
}
