package woodle.backend.test.persistence;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.controller.MemberRegistration;
import woodle.backend.entity.Principle;
import woodle.backend.entity.Role;
import woodle.backend.test.rest.RestClientTest;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


@RunWith(Arquillian.class)
public class MemberRegistrationTest {

    public static final String JANE_MAILINATOR_COM = "jane@mailinator.com";
    public static final String TOP_SECRET = "topSecret";

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "memberRegistration.war")
                .addPackage(Principle.class.getPackage())
                .addPackage(MemberRegistration.class.getPackage())
                .addPackage("woodle.backend.model")
                .addPackage("woodle.backend.data")
                .addPackage("woodle.backend.rest")
                .addPackage("org.apache.commons.logging")
                .addClass(RestClientTest.class)
                .addPackages(true, "org.apache.http")
                .addAsWebInfResource(new File(RestClientTest.SRC_MAIN_WEBAPP_WEB_INF + "/web.xml"))
                .addAsWebInfResource(new File(RestClientTest.SRC_MAIN_WEBAPP_WEB_INF + "/jboss-web.xml"))
                .merge(RestClientTest.PERSISTENCE())
                .merge(RestClientTest.COMMON());

    }

    @Inject
    MemberRegistration memberRegistration;

    @Resource(lookup = "java:jboss/datasources/WoodleDS")
    private javax.sql.DataSource employeeDataSource;

    @Inject
    Logger log;

    @Test
    public void testRegister() throws Exception {

        memberRegistration.reset();

        Principle principle = new Principle(JANE_MAILINATOR_COM, TOP_SECRET);
        principle.getRoles().add(new Role("known"));
        memberRegistration.register(principle);

        principle = new Principle("santa@claus.no", "secret");
        principle.getRoles().add(new Role("known"));

        memberRegistration.register(principle);
        Connection connection = employeeDataSource.getConnection();
        PreparedStatement preparedStatement1 = connection.prepareStatement("select password from  PRINCIPLES");
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        while (resultSet1.next()) {
            System.out.println(resultSet1.getString(1));
        }

        PreparedStatement preparedStatement = connection.prepareStatement("select password from  PRINCIPLES where principal_id=?");
        preparedStatement.setString(1, JANE_MAILINATOR_COM);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        assertThat(resultSet.getString(1), is(equalTo(TOP_SECRET)));

        Principle jane = memberRegistration.lookup(JANE_MAILINATOR_COM);
        assertThat(jane.getRoles().iterator().next().getUser_role(), equalTo(new Role("known").getUser_role()));


    }
}
