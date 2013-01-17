package woodle.backend.service;

import woodle.backend.entity.Principle;
import woodle.backend.model.Member;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;
    @Inject
    private EntityManager em;
    @Inject
    private Event<Member> memberEventSrc;

    public void register(Member member) throws Exception {
        log.info("Registering " + member.toString());
        em.persist(member);
        em.persist(new Principle(member));
        memberEventSrc.fire(member);
    }

    public void login(Principle principle) {
        log.info("Logging in " + principle.toString());
    }
}
