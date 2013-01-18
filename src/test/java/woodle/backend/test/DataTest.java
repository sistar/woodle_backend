package woodle.backend.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Attendance;
import woodle.backend.util.Resources;

import javax.inject.Inject;
import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class DataTest {

    @Inject
    WoodleStore woodleStore;

    @Deployment(testable = true)
    public static WebArchive getTestArchive() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "dataTest.war")
                .addPackage("woodle.backend.entity")
                .addPackage("woodle.backend.model")
                .addPackage("woodle.backend.data")
                .addClasses(Resources.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml")
                .addAsWebInfResource("jboss-deployment-structure-test.xml", "jboss-deployment-structure.xml");
        //System.out.println(webArchive.toString(true));
        return webArchive;

    }

    @Test
    public void testAddAppointment() throws Exception {
        Appointment appointment = new Appointment("theId", "someTitle", "location", "description", "2012-02-10", "2012-02-10",
                new TreeSet<Attendance>(), new TreeSet<Attendance>(), "ralf.sigmund@gmail.com", 1);

        woodleStore.saveAppointment(appointment);
        assertThat(woodleStore.getAppointmentMap().size(), is(equalTo(1)));
        assertThat(woodleStore.appointmentsForUser("ralf.sigmund@gmail.com").size(), is(equalTo(1)));

    }
}
