package woodle.backend.data;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentKey;

import javax.annotation.Nullable;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class AppointmentRepository {

    @Inject
    private Logger log;
    @Inject
    private EntityManager em;

    public Map<AppointmentKey, Appointment> appointmentMap() {
        Query query = em.createQuery("select a from Appointment a", Appointment.class);

        List<Appointment> resultList = query.getResultList();

        Map<AppointmentKey, Appointment> mappedRoles = Maps.uniqueIndex(resultList, new Function<Appointment, AppointmentKey>() {

            @Override
            public AppointmentKey apply(@Nullable Appointment input) {
                return new AppointmentKey(input.getTitle(), input.getStartDate(), input.getUser());
            }
        });
        return mappedRoles;
    }

    public void store(AppointmentKey appointmentKey, Appointment appointment) {
        em.persist(appointment);
    }

    public List<Appointment> appointmentsForUser(String eMail) {
        Query query = em.createQuery("select a from Appointment a where a.user = :eMail", Appointment.class);
        query.setParameter("eMail", eMail);
        return query.getResultList();
    }

    public List<Appointment> appointmentsAttendanceOrMaybeAttendance(String eMail) {
        Query query = em.createQuery("select a from Appointment a  where a.attendances.attendantEmail = :eMail or a.maybeAttendances.attendantEmail = :eMail", Appointment.class);
        query.setParameter("eMail", eMail);
        return query.getResultList();
    }

    public List<Appointment> appointmentsMaybeAttendance(String eMail) {

        Query query = em.createQuery("select a from Appointment a  where a.maybeAttendances.attendantEmail = :eMail", Appointment.class);
        query.setParameter("eMail", eMail);
        return query.getResultList();
    }

    public List<Appointment> appointmentsAttendanceConfirmed(String eMail) {
        Query query = em.createQuery("select a from Appointment a  where a.attendances.attendantEmail = :eMail", Appointment.class);
        query.setParameter("eMail", eMail);
        return query.getResultList();
    }

    public Appointment appointmentForId(String creatorEmail, String appointmentId) {
        Query query = em.createQuery("select a from Appointment a  where a.user = :creatorEmail and a.id = :appointmentId", Appointment.class);
        query.setParameter("creatorEmail", creatorEmail);
        query.setParameter("appointmentId", appointmentId);

        return (Appointment) query.getSingleResult();
    }

    public Appointment appointmentForKey(AppointmentKey appointmentKey) {
        return appointmentForId(appointmentKey.getCreatorEmail(), appointmentKey.getId());
    }

    public void delete() {
        Query query = em.createQuery("DELETE FROM Appointment a");
    }

    public void delete(AppointmentKey appointmentKey) {
        Query query = em.createQuery("DELETE FROM Appointment a where a.user = :creatorEmail and a.id =:appointmentId");
        query.setParameter("creatorEmail", appointmentKey.getCreatorEmail());
        query.setParameter("appointmentId", appointmentKey.getId());
        query.executeUpdate();

    }
}
