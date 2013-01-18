package woodle.backend.rest;

import woodle.backend.data.MemberRepository;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Member;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@RequestScoped
public class RegisterResourceService implements RegisterResource {
    public static final String APPLICATION_JSON = "application/json";

    @Inject
    Logger logger;

    @Inject
    private WoodleStore woodleStore;

    @Inject
    private MemberRepository memberRepository;

    @Override
    public void createMember(Member member) {

        if (member.getEmail() == null || member.getEmail().isEmpty()) {
            logger.severe("email must not be null or empty");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (memberRepository.existsMember(member.getEmail())) {
            logger.severe(String.format("allready in store: %s", member.getEmail()));
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        if (memberRepository.existsPrinciple(member.getEmail())) {
            logger.severe(String.format("allready in registration: %s", member.getEmail()));
            throw new WebApplicationException(Response.Status.CONFLICT);
        }

        logger.info("storing new member: " + member);
        woodleStore.saveMember(member);
    }

}
