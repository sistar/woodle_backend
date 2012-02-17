package woodle.backend.data;

import woodle.backend.model.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.logging.Logger;

@ApplicationScoped
public class WoodleStore {

    Logger log = Logger.getLogger(this.getClass().getName());


    Map<AppointmentKey, Appointment> appointmentMap = new HashMap<AppointmentKey, Appointment>();
    Map<String, Member> memberMap = new HashMap<String, Member>();

    public Map<AppointmentKey, Appointment> getAppointmentMap() {
        return appointmentMap;
    }

    public List<Appointment> appointmentsForUser(String eMail) {
        List<Appointment> res = new ArrayList<Appointment>();
        for (AppointmentKey k : appointmentMap.keySet()) {
            if (k.getCreatorEmail().equals(eMail)) {
                res.add(appointmentMap.get(k));
            }
        }
        return res;
    }

    public void saveAppointment(Appointment appointment) throws UnkownMemberException {
        if (!memberMap.containsKey(appointment.getUser())) throw new UnkownMemberException(appointment.getUser());
        log.info("APPOINTMENT MAP SIZE BEFORE:" + appointmentMap.size());
        appointmentMap.put(appointment.createAppointmentKey(), appointment);
        log.info("APPOINTMENT MAP SIZE AFTER:" + appointmentMap.size());
    }

    public boolean hasMember(String eMail) {
        return memberMap.containsKey(eMail);
    }

    public void saveMember(Member member) {
        memberMap.put(member.getEmail(), member);
    }

    public void resetMembers() {
        memberMap.clear();
    }

    public Collection<Member> getMembers() {
        return memberMap.values();
    }

    public Member getMember(String email) {
        return memberMap.get(email);
    }

    public List<Appointment> appointmentsAttendance(String eMail) {
        List<Appointment> res = new ArrayList<Appointment>();
        for (Appointment a : appointmentMap.values()) {
            if (emails(a.getAttendances()).contains(eMail) || emails(a.getMaybeAttendances()).contains(eMail)) {
                res.add(a);
            }
        }
        return res;
    }

    private List<String> emails(Collection<ComparableAttendance> attendances) {
        ArrayList<String> res = new ArrayList<String>();
        for (Attendance attendance : attendances) {
            res.add(attendance.getAttendantEmail());
        }
        return res;
    }

    public List<Appointment> appointmentsAttendanceWaiting(String eMail) {
        List<Appointment> res = new ArrayList<Appointment>();
        for (Appointment a : appointmentMap.values()) {
            if (emails(a.getMaybeAttendances()).contains(eMail)) {
                res.add(a);
            }
        }
        return res;
    }

    public List<Appointment> appointmentsAttendanceConfirmed(String eMail) {
        List<Appointment> res = new ArrayList<Appointment>();
        for (Appointment a : appointmentMap.values()) {
            if (emails(a.getAttendances()).contains(eMail)) {
                res.add(a);
            }
        }
        return res;
    }

    public Appointment lookUpAppointment(Appointment appointment) {
        return appointmentMap.get(appointment.createAppointmentKey());
    }

    public String attend(String appointmentId, Attendance attendance) {
        Appointment appointment = appointmentMap.get(Appointment.createAppointmentKey(attendance.getCreatorEmail(), appointmentId));
        return appointment.attend(attendance);
    }

    public String cancel(String appointmentId, String creatorEmail, String userEmail) {
        Appointment appointment = appointmentMap.get(Appointment.createAppointmentKey(creatorEmail, appointmentId));
        return appointment.cancel(userEmail);

    }

    public void deleteAppointment(String appointmentId, String creatorEmail) {
        AppointmentKey appointmentKey = Appointment.createAppointmentKey(creatorEmail, appointmentId);
        Appointment appointment = appointmentMap.get(appointmentKey);
        appointmentMap.remove(appointmentKey);
    }

    public void resetAppointments() {
        appointmentMap.clear();
    }
}
