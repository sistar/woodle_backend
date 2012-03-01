package woodle.backend.test.rest;


import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.data.WoodleStore;
import woodle.backend.rest.MemberResource;
import woodle.backend.rest.RegisterResource;


@RunWith(Arquillian.class)
public class MemberRegistrationClientTest extends RestClientTest {

    public static final String JANE_MAILINATOR_COM = "jane@mailinator.com";
    public static final String TOP_SECRET = "topSecret";


    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "woodle_backend.war").
                addPackage("woodle.backend.model")
                .addPackage("woodle.backend.rest")
                .addClasses(WoodleStore.class)
                .merge(COMMON())
                .merge(AUTHENTICATION())
                .merge(PERSISTENCE());
    }


    @Test
    public void testRegisterAndAuthenticate() throws Exception {
        resetCreateDefaultUser();
        Assert.assertThat(client(MemberResource.class, SANTA_CLAUS_NO, "secret").listAllMembers().size(), CoreMatchers.is(CoreMatchers.equalTo(1)));
        createMember(client(RegisterResource.class), JANE_MAILINATOR_COM, TOP_SECRET);
        Assert.assertThat(client(MemberResource.class, JANE_MAILINATOR_COM, TOP_SECRET).listAllMembers().size(), CoreMatchers.is(CoreMatchers.equalTo(2)));
    }

}
