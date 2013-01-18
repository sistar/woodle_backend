package woodle.backend.data;

import woodle.backend.entity.Principle;
import woodle.backend.model.Member;
import woodle.backend.model.Member_;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import static woodle.backend.data.WoodleStore.sha256Base64;

@Stateless
public class MemberRepository {

    @Inject
    private Logger log;
    @Inject
    private EntityManager em;

    public void register(Member member) {
        Principle newPrinciple = null;
        try {
            newPrinciple = new Principle(member.getEmail(), sha256Base64((member.getPassword())));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info(String.format("Registering member with E-Mail %s ", newPrinciple.getId(), newPrinciple.getPassword()));
        em.persist(newPrinciple);
    }

    public Principle lookup(String email) throws Exception {
        Query query = em.createQuery("select principle from Principle as principle left join FETCH principle.roles where principle.id = :id", Principle.class);
        query.setParameter("id", email);
        return (Principle) query.getSingleResult();

    }

    public Member lookupMember(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public boolean existsMember(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(cb.count(member)).where(cb.equal(member.get(Member_.email), email));
        return (em.createQuery(criteria).getSingleResult() > 0L);
    }

    public boolean existsPrinciple(String email) {
        Query query = em.createQuery("select count( principle ) from Principle as principle where principle.id = :id");
        query.setParameter("id", email);
        return ((Number) query.getSingleResult()).intValue() > 0;
    }

    public void reset() {
        em.createNativeQuery("DELETE FROM ROLES").executeUpdate();
        em.createQuery("delete from Principle").executeUpdate();
        em.createQuery("delete from Member").executeUpdate();
    }

    public List<Member> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(member).orderBy(cb.asc(member.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
