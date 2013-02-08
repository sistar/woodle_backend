package woodle.backend.data;

import sun.misc.BASE64Encoder;
import woodle.backend.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class WoodleStore {

    @Inject
    MemberRepository memberRepository;
    @Inject
    AppointmentRepository appointmentRepository;
    @Inject
    Logger log;

    public static String sha256Base64(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        digest.reset();
        byte[] bytes = new byte[0];
        try {
            bytes = digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return byteToBase64(bytes);
    }

    /**
     * From a byte[] returns a base 64 representation
     *
     * @param data byte[]
     * @return String
     */
    public static String byteToBase64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public Map<AppointmentKey, Appointment> getAppointmentMap() {
        return appointmentRepository.appointmentMap();
    }

    public List<Appointment> appointmentsForUser(String eMail) {
        return appointmentRepository.appointmentsForUser(eMail);

    }

    public void saveAppointment(Appointment appointment) throws UnkownMemberException {

        if (!memberRepository.existsMember(appointment.getUser()))
            throw new UnkownMemberException(appointment.getUser());

        appointmentRepository.store(appointment.createAppointmentKey(), appointment);

    }

    public void saveMember(Member member) {
        memberRepository.register(member);
    }

    public void resetMembers() {
        log.info("RESETTING members");
        memberRepository.reset();
    }

    public Collection<Member> getMembers() {
        return memberRepository.findAllOrderedByName();
    }

    public Member getMember(String email) {
        return memberRepository.lookupMember(email);
    }

    public List<Appointment> appointmentsAttendance(String eMail) {
        return appointmentRepository.appointmentsAttendanceOrMaybeAttendance(eMail);

    }

    private List<String> emails(Collection<Attendance> attendances) {
        ArrayList<String> res = new ArrayList<String>();
        for (Attendance attendance : attendances) {
            res.add(attendance.getAttendantEmail());
        }
        return res;
    }

    public List<Appointment> appointmentsAttendanceWaiting(String eMail) {
        return appointmentRepository.appointmentsMaybeAttendance(eMail);
    }

    public List<Appointment> appointmentsAttendanceConfirmed(String eMail) {
        return appointmentRepository.appointmentsAttendanceConfirmed(eMail);
    }

    public String attend(String creatorEmail,
                         String appointmentId,
                         Attendance attendance) throws UnkownAppointmentException {
        appointmentRepository.appointmentForId(creatorEmail, appointmentId);
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
        return appointmentRepository.appointmentForKey(appointmentKey).attend(attendance);
    }

    @Deprecated
    public String cancel(String appointmentId,
                         String creatorEmail,
                         String userEmail) throws UnkownAppointmentException {
        return appointmentRepository.appointmentForId(creatorEmail, appointmentId).cancel(userEmail);

    }

    public String cancel(AppointmentKey appointmentKey,
                         String attendantEmail) throws UnkownAppointmentException {
        return appointmentRepository.appointmentForKey(appointmentKey).cancel(attendantEmail);
    }

    @Deprecated
    public void deleteAppointment(String appointmentId, String creatorEmail) {
        deleteAppointment(Appointment.createAppointmentKey(creatorEmail, appointmentId));
    }

    public void deleteAppointment(AppointmentKey appointmentKey) {
        appointmentRepository.delete(appointmentKey);
    }

    public void resetAppointments() {
        appointmentRepository.delete();
    }

}
