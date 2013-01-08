package woodle.backend.controller;

import woodle.backend.entity.Principle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.logging.Logger;

@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void register(Principle newPrinciple) {
        log.info(String.format("Registering member with E-Mail %s ", newPrinciple.getId(), newPrinciple.getPassword()));
        em.persist(newPrinciple);
    }

    public Principle lookup(String email) throws Exception {
        Query query = em.createQuery("select principle from Principle as principle left join FETCH principle.roles where principle.id = :id", Principle.class);
        query.setParameter("id", email);
        return (Principle) query.getSingleResult();

    }

    public boolean hasMember(String email) {
        Query query = em.createQuery("select count( principle ) from Principle as principle where principle.id = :id");
        query.setParameter("id", email);
        return ((Number) query.getSingleResult()).intValue() > 0;
    }

    public void reset() {
        em.createNativeQuery("delete from ROLES").executeUpdate();
        em.createQuery("delete from Principle").executeUpdate();
    }
}
