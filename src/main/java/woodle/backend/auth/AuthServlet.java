package woodle.backend.auth;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(AuthServlet.URL_PATTERN)
public class AuthServlet extends javax.servlet.http.HttpServlet {
    public static final String USER_PARAM = "username";
    public static final String PASSWORD_PARAM = "password";
    public static final String URL_PATTERN = "/auth";

    @Inject
    Logger logger;

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String password = request.getParameterMap().get(PASSWORD_PARAM)[0];
        String username = request.getParameterMap().get(USER_PARAM)[0];
        logger.info(String.format(">>AUTH: user %s : password %s", username, password));
        request.login(username, password);
        response.sendRedirect("test.jsf");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
