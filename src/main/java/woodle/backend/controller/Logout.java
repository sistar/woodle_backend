package woodle.backend.controller;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.logging.Logger;

@Model
public class Logout {

    @Inject
    Logger logger;

    @Inject
    MemberController memberController;

    @Inject
    private FacesContext facesContext;

    public String logout() {
        logger.info(String.format("LOGGING USER OUT"));
        memberController.initNewMember();
        facesContext.getExternalContext().invalidateSession();

        return "logout.jsf?faces-redirect=true";
    }
}
