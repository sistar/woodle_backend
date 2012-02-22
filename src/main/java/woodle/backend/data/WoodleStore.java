package woodle.backend.data;

import woodle.backend.controller.MemberRegistration;
import woodle.backend.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

@ApplicationScoped
public class WoodleStore {
    @Inject
    MemberRegistration memberRegistration;

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
        //memberRegistration.register(new Principle(member.getEmail(),member.getPassword()));

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

    public String attend(String creatorEmail, String title, String startDate,
                         Attendance attendance) throws UnkownAppointmentException {
        AppointmentKey appointmentKey = Appointment.createAppointmentKey(creatorEmail, title, startDate);
        return attend(appointmentKey, attendance);
    }

    public String attend(String creatorEmail,
                         String appointmentId,
                         Attendance attendance) throws UnkownAppointmentException {
        AppointmentKey appointmentKey = Appointment.createAppointmentKey(creatorEmail, appointmentId);
        return attend(appointmentKey, attendance);
    }

    public String attend(String creatorEmail,
                         String title,
                         String startDate,
                         String calendarEventId,
                         String attendendEmail) throws UnkownAppointmentException {
        AppointmentKey appointmentKey = Appointment.createAppointmentKey(creatorEmail, title, startDate);
        Attendance attendance = new Attendance(attendendEmail, calendarEventId);
        return attend(appointmentKey, attendance);
    }


    private String attend(AppointmentKey appointmentKey, Attendance attendance) throws UnkownAppointmentException {
        return nullSafeAppointmentForKey(appointmentKey).attend(attendance);
    }

    @Deprecated
    private Appointment nullSafeAppointmentForAppointmentId(String creatorEmail,
                                                            String appointmentId) throws UnkownAppointmentException {

        AppointmentKey appointmentKey = Appointment.createAppointmentKey(creatorEmail, appointmentId);
        Appointment appointment = appointmentMap.get(appointmentKey);
        if (appointment == null) {
            throw new UnkownAppointmentException(appointmentKey.getId());
        }
        return appointment;
    }

    @Deprecated
    public String cancel(String appointmentId,
                         String creatorEmail,
                         String userEmail) throws UnkownAppointmentException {
        return nullSafeAppointmentForAppointmentId(creatorEmail, appointmentId).cancel(userEmail);

    }

    public String cancel(AppointmentKey appointmentKey,
                         String attendantEmail) throws UnkownAppointmentException {
        return nullSafeAppointmentForKey(appointmentKey).cancel(attendantEmail);
    }

    @Deprecated
    public void deleteAppointment(String appointmentId, String creatorEmail) {
        deleteAppointment(Appointment.createAppointmentKey(creatorEmail, appointmentId));
    }

    public void deleteAppointment(AppointmentKey appointmentKey) {
        appointmentMap.remove(appointmentKey);
    }

    public void resetAppointments() {
        appointmentMap.clear();
    }

    private Appointment nullSafeAppointmentForKey(AppointmentKey appointmentKey) throws UnkownAppointmentException {
        Appointment appointment = appointmentMap.get(appointmentKey);
        if (appointment == null) {
            throw new UnkownAppointmentException(appointmentKey.getId());
        }
        return appointment;
    }
}
