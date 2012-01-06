package woodle.backend.test;


import java.net.URL;

import javax.ws.rs.ApplicationPath;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.data.UnkownMemberException;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.Member;
import woodle.backend.rest.AppointmentsResource;
import woodle.backend.rest.JaxRsActivator;
import woodle.backend.rest.MemberResourceRESTService;
import woodle.backend.test.resource.AppointmentClient;
import woodle.backend.test.resource.MemberClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@RunWith(Arquillian.class)
@RunAsClient
public class AppointmentClientTest {
    private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);
    public static final String SANTA_CLAUS_NO = "santa@claus.no";

    public static final XMLGregorianCalendar APPOINTMENT_DATE;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "woodle.backend.war").addPackage(Appointment.class.getPackage())
                .addClasses(AppointmentsResource.class,
                        MemberResourceRESTService.class,
                        JaxRsActivator.class,
                        woodle.backend.data.WoodleStore.class,
                        UnkownMemberException.class)
                       // .addAsManifestResource("persistence.xml")
                        .addAsResource( "META-INF/persistence.xml")
                        //.addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;


    @BeforeClass
    public static void initResteasyClient() {
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

   static {

        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {

        }
        APPOINTMENT_DATE = dtf.newXMLGregorianCalendar("2012-02-01T13:10:00");
    }

    AppointmentClient appointmentClient;

    MemberClient memberClient;

    @Test
    public void testPutAppointmentUsingClientProxy() throws Exception {
        appointmentClient = ProxyFactory.create(AppointmentClient.class, deploymentUrl.toString() + RESOURCE_PREFIX);
        memberClient = ProxyFactory.create(MemberClient.class, deploymentUrl.toString() + RESOURCE_PREFIX);
        createMember(memberClient);
        createAppointment(appointmentClient);
        AppointmentListing appointments = memberClient.lookupAppointmentsForMemberEMail(SANTA_CLAUS_NO);
        assertThat(appointments.getAppointments().iterator().next().getStart(), is(equalTo(APPOINTMENT_DATE)));
    }

    public static void createAppointment(AppointmentClient appointmentClient) {
        appointmentClient.create(new Appointment("json Hacking", APPOINTMENT_DATE, SANTA_CLAUS_NO));
    }

    public static void createMember( MemberClient memberClient) {
        Member member = new Member();
        member.setEmail(SANTA_CLAUS_NO);
        memberClient.addMember(member);
    }


}