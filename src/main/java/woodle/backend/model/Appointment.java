package woodle.backend.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlRootElement(name = "appointment")
public class Appointment {

    private String title;
    private XMLGregorianCalendar start;
    private String memberEmail;

    public Appointment() {
    }

    public Appointment(String title, XMLGregorianCalendar start, String memberEmail) {
        this.title = title;
        this.start = start;
        this.memberEmail = memberEmail;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public XMLGregorianCalendar getStart() {
        return start;
    }

    public void setStart(XMLGregorianCalendar start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "title='" + title + '\'' +
                ", start=" + start +
                '}';
    }


}
