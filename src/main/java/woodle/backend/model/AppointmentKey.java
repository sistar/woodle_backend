package woodle.backend.model;

import javax.xml.datatype.XMLGregorianCalendar;

public class AppointmentKey {
    private String title;
    private XMLGregorianCalendar start;

    public String getTitle() {
        return title;
    }

    public XMLGregorianCalendar getStart() {
        return start;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    private String memberEmail;

    public AppointmentKey(String title, XMLGregorianCalendar start, String memberEmail
    ) {
        this.title = title;
        this.start = start;
        this.memberEmail = memberEmail;

    }

    public AppointmentKey(Appointment appointment) {
        this.title = appointment.getTitle();
        this.start = appointment.getStartDate();
        this.memberEmail = appointment.getUser();

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentKey)) return false;

        AppointmentKey that = (AppointmentKey) o;

        if (memberEmail != null ? !memberEmail.equals(that.memberEmail) : that.memberEmail != null) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (memberEmail != null ? memberEmail.hashCode() : 0);
        return result;
    }
}
