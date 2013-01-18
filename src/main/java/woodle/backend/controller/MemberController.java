package woodle.backend.controller;

import woodle.backend.entity.Principle;
import woodle.backend.model.Member;
import woodle.backend.service.MemberRegistration;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class MemberController implements Serializable {

    @Inject
    private FacesContext facesContext;
    @Inject
    private MemberRegistration memberRegistration;
    @Inject
    Logger logger;
    @Produces
    @Named
    private Member newMember;
    @Produces
    @Named
    private String username;
    @Produces
    @Named
    private String searchEnabled = "disabled";
    @Produces
    @Named
    private boolean searchEnabledJsf = false;
    @Produces
    @Named
    private Principle principle;

    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
        principle = new Principle();
        searchEnabled = "disabled";
        searchEnabledJsf = false;
        username = "";
    }

    public void register() throws Exception {
        try {
            memberRegistration.register(newMember);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public String login() throws Exception {
        try {
            logger.info(String.format("LOGGING USER IN"));
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            request.login(principle.getId(), principle.getPassword());

            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged in!", "Login successful");
            facesContext.addMessage(null, m);
            //initNewMember();
            searchEnabled = "active";
            searchEnabledJsf = true;
            username = principle.getId();
            logger.info(String.format("LOG IN COMPLETE FOR %s", username));
            return "main";
        } catch (Exception e) {

            String errorMessage = getRootErrorMessage(e);
            logger.info(String.format("LOG IN FAILED [USER %s, PW %s] %s", principle.getId(), principle.getPassword(), e.getMessage()));
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Login unsuccessful");
            facesContext.addMessage(null, m);
            return "index";
        }
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
