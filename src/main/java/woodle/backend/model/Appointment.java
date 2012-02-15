package woodle.backend.model;


import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

public class Appointment {
    private String id;
    private String title;
    private String location;
    private String description;
    private XMLGregorianCalendar startDate;
    private XMLGregorianCalendar endDate;
    private List<String> attendance;
    private List<String> maybeAttendance;
    private String user;
    private int maxNumber;

    public Appointment() {
    }

    public Appointment(String id, String title, String location, String description,
                       XMLGregorianCalendar startDate, XMLGregorianCalendar endDate,
                       List<String> attendance, List<String> maybeAttendance, String user, int maxNumber) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendance = attendance;
        this.maybeAttendance = maybeAttendance;
        this.user = user;
        this.maxNumber = maxNumber;
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


    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(XMLGregorianCalendar startDate) {
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

    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    public void setEndDate(XMLGregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public List<String> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<String> attendance) {
        this.attendance = attendance;
    }

    public List<String> getMaybeAttendance() {
        return maybeAttendance;
    }

    public void setMaybeAttendance(List<String> maybeAttendance) {
        this.maybeAttendance = maybeAttendance;
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
                ", attendance=" + attendance +
                ", maybeAttendance=" + maybeAttendance +
                ", user='" + user + '\'' +
                ", maxNumber=" + maxNumber +
                '}';
    }
}
