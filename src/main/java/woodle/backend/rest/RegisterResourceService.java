package woodle.backend.rest;

import woodle.backend.controller.MemberRegistration;
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
    private MemberRegistration memberRegistration;

    @Override
    public void createMember(Member member) {

        if (member.getEmail() == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (woodleStore.hasMember(member.getEmail())) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        if (memberRegistration.hasMember(member.getEmail())) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }


        logger.info("storing new member: " + member);
        woodleStore.saveMember(member);
    }

}
