package woodle.backend.test.persistence;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.controller.MemberRepository;
import woodle.backend.entity.Principle;
import woodle.backend.test.rest.RestClientTest;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class MemberRegistrationTest {

    public static final String JANE_MAILINATOR_COM = "jane@mailinator.com";
    public static final String TOP_SECRET = "topSecret";
    public static final String KNOWN = "known";
    @Inject
    MemberRepository memberRepository;
    @Resource(lookup = "java:jboss/datasources/WoodleDS")
    private javax.sql.DataSource employeeDataSource;
    private Connection connection;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "memberRegistration.war")
                .addPackage(Principle.class.getPackage())
                .addPackage(MemberRepository.class.getPackage())
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

    @Before
    public void setUp() throws Exception {
        memberRepository.reset();

        this.connection = employeeDataSource.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        this.connection.close();

    }

    @Test
    public void testThatMemberRegistrationIsCleared() throws Exception {

        registerDummyUser();

        assertMembersRegistrationEmpty(false);
        memberRepository.reset();
        assertMembersRegistrationEmpty(true);
    }

    @Test
    public void testThatMemberIsRegistered() throws Exception {

        registerDummyUser();

        PreparedStatement preparedStatement1 = connection.prepareStatement("select PRINCIPAL_ID, password from  PRINCIPLES");
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        boolean next = resultSet1.next();

        assertThat(resultSet1.getString(1), is(JANE_MAILINATOR_COM));
        assertThat(resultSet1.getString(2), is(TOP_SECRET));

    }

    @Test
    public void testThatDummyUserHasDummyRole() throws Exception {

        assertRows("ROLES", 0);
        registerDummyUser();
        assertRows("ROLES", 1);

        PreparedStatement preparedStatement1 = connection.prepareStatement("select  principal_id,user_role ,role_group from  ROLES");
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();

        assertThat(resultSet1.getString(1), is(JANE_MAILINATOR_COM));
        assertThat(resultSet1.getString(2), is(KNOWN));

    }

    private void assertRows(String tableName, int numberOfRows) throws SQLException {

        PreparedStatement preparedStatement1 = connection.prepareStatement(String.format("select count(*) from %s", tableName));
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        assertThat(resultSet1.getInt(1), is(numberOfRows));
    }

    private void registerDummyUser() {
        Principle principle = new Principle(JANE_MAILINATOR_COM, TOP_SECRET);
        memberRepository.register(principle);
    }

    private void assertMembersRegistrationEmpty(boolean isEmpty) throws SQLException {
        boolean expectNext = !isEmpty;

        PreparedStatement preparedStatement1 = connection.prepareStatement("select password from  PRINCIPLES");
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        boolean hasNext = resultSet1.next();
        assertThat(hasNext, is(expectNext));
    }

}
