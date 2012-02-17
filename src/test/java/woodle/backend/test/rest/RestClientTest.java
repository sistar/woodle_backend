package woodle.backend.test.rest;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import woodle.backend.model.Appointment;
import woodle.backend.model.Member;
import woodle.backend.rest.AppointmentResource;
import woodle.backend.rest.JaxRsActivator;
import woodle.backend.rest.RegisterResource;

import javax.ws.rs.ApplicationPath;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class RestClientTest {
    public static final String SANTA_CLAUS_NO = "santa@claus.no";
    public static final String JSON_HACKING = "json Hacking";
    public static final String APPOINTMENT_DATE = "2012-02-01T13:10:00.000+01:00";
    public static final String APPOINTMENT_ID = JSON_HACKING + "-" + APPOINTMENT_DATE;


    public static final String SRC_MAIN_RESOURCES = "src/main/resources";
    public static final String SRC_MAIN_WEBAPP_WEB_INF = "src/main/webapp/WEB-INF";
    /**
     * module providing basic authentication
     */
    public static final Archive<WebArchive> AUTHENTICATION = ShrinkWrap.create(WebArchive.class)
            .addAsResource(new File(SRC_MAIN_RESOURCES + "/roles.properties"))
            .addAsResource(new File(SRC_MAIN_RESOURCES + "/users.properties"))
            .addAsWebInfResource(new File(SRC_MAIN_WEBAPP_WEB_INF + "/web.xml"))
            .addAsWebInfResource(new File(SRC_MAIN_WEBAPP_WEB_INF + "/jboss-web.xml"));
    protected static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);


    @ArquillianResource
    URL deploymentUrl;

    /**
     * @param clazz
     * @return a Rest Client to clazz
     */
    protected <T> T client(Class<? extends T> clazz, String userEmail, String password) {
        String path = deploymentUrl.toString() + RESOURCE_PREFIX;

        Credentials credentials = new UsernamePasswordCredentials(userEmail, password);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY),
                credentials);
        ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);


        return ProxyFactory.create(clazz,
                path, clientExecutor);
    }

    public Member createMember(RegisterResource registerClient, String email, String password) {
        Member member = new Member();
        member.setEmail(email);
        member.setPassword(password);
        //store member to woodle backend
        registerClient.createMember(member);

        return member;
    }

    public Appointment createAppointment(AppointmentResource appointmentClient) {
        Appointment appointment = new Appointment(
                APPOINTMENT_ID,
                JSON_HACKING,
                "North Pole",
                "icy JSON stuff",
                APPOINTMENT_DATE,
                APPOINTMENT_DATE,
                Arrays.asList(SANTA_CLAUS_NO),
                Arrays.asList("rupert@north.pole"),
                SANTA_CLAUS_NO, 1);
        //store appointment to woodle backend
        appointmentClient.create(appointment);

        return appointment;
    }
}
