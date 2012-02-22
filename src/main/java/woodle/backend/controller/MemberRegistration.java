package woodle.backend.controller;

import woodle.backend.entity.Principle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void register(Principle newPrinciple) throws Exception {
        log.info("Registering member with E-Mail " + newPrinciple.getId());
        em.persist(newPrinciple);
    }

}
