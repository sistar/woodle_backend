package woodle.backend.data;

import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentKey;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.Member;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class WoodleStore {
    Map<AppointmentKey, Appointment> appointmentMap = new HashMap<AppointmentKey, Appointment>();
    Map<String, Member> memberMap = new HashMap<String, Member>();

    public Map<AppointmentKey, Appointment> getAppointmentMap() {
        return appointmentMap;
    }

    public AppointmentListing appointmentsForUser(String eMail) {
        Set<Appointment> res = new HashSet<Appointment>();
        for (AppointmentKey k : appointmentMap.keySet()) {
            if (k.getMemberEmail().equals(eMail)) {
                res.add(appointmentMap.get(k));
            }
        }
        return new AppointmentListing(res);
    }

    public void saveAppointment(Appointment appointment) throws UnkownMemberException {
        if(! memberMap.containsKey(appointment.getMemberEmail())) throw new UnkownMemberException(appointment.getMemberEmail());
        appointmentMap.put(new AppointmentKey(appointment), appointment);
    }

    public boolean hasMember(String eMail){
        return memberMap.containsKey(eMail);
    }

    public void saveMember(Member member) {
        memberMap.put(member.getEmail(),member);
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
}
