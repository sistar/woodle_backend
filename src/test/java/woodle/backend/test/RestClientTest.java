package woodle.backend.test;

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
import org.junit.Before;
import woodle.backend.model.Appointment;
import woodle.backend.model.Member;
import woodle.backend.rest.AppointmentResource;
import woodle.backend.rest.JaxRsActivator;
import woodle.backend.rest.ManagementResource;
import woodle.backend.rest.MemberResource;

import javax.ws.rs.ApplicationPath;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

public class RestClientTest {
    public static final String SANTA_CLAUS_NO = "santa@claus.no";
    public XMLGregorianCalendar APPOINTMENT_DATE;


    /**
     * module providing basic authentication
     */
    public static final Archive<WebArchive> AUTHENTICATION = ShrinkWrap.create(WebArchive.class)
            .addAsResource("roles.properties")
            .addAsResource("users.properties")
            .addAsWebInfResource("WEB-INF/web.xml")
            .addAsWebInfResource("WEB-INF/jboss-web.xml");
    protected static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);
    @ArquillianResource
    URL deploymentUrl;


    AppointmentResource appointmentClient;
    MemberResource memberClient;
    ManagementResource managementResource;

    @Before
    public void init() {
        DatatypeFactory dtf;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        APPOINTMENT_DATE = dtf.newXMLGregorianCalendar("2012-02-01T13:10:00.000+01:00");

        appointmentClient = client(AppointmentResource.class);
        memberClient = client(MemberResource.class);
        managementResource = client(ManagementResource.class);
    }

    /**
     * @param clazz
     * @param <T>
     * @return a Rest Client to clazz
     */
    protected <T> T client(Class<? extends T> clazz) {
        String path = deploymentUrl.toString() + RESOURCE_PREFIX;

        Credentials credentials = new UsernamePasswordCredentials("testuser", "secret");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY),
                credentials);
        ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);


        return ProxyFactory.create(clazz,
                path, clientExecutor);
    }

    public Member createMember(MemberResource memberClient) {
        Member member = new Member();
        member.setEmail(SANTA_CLAUS_NO);
        member.setPassword("topSecret");
        assertNotNull(memberClient);
        memberClient.addMember(member);
        return member;
    }

    public Appointment createAppointment(AppointmentResource appointmentClient) {
        Appointment appointment = new Appointment("json Hacking-2012-21-03-16.20.00", "json Hacking", "North Pole", "icy JSON stuff",
                APPOINTMENT_DATE, APPOINTMENT_DATE, Arrays.asList(SANTA_CLAUS_NO), Arrays.asList("rupert@north.pole"), SANTA_CLAUS_NO, 1);
        appointmentClient.create(appointment);
        return appointment;

    }
}
