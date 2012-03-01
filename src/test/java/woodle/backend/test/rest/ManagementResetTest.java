package woodle.backend.test.rest;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.data.WoodleStore;
import woodle.backend.rest.JaxRsActivator;
import woodle.backend.rest.ManagementResource;
import woodle.backend.rest.ManagementResourceService;
import woodle.backend.util.Resources;

@RunWith(Arquillian.class)
public class ManagementResetTest extends RestClientTest {

    @Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "woodle_backend.war")
                .addPackage("woodle.backend.model")
                .addClasses(ManagementResource.class,
                        ManagementResourceService.class,
                        WoodleStore.class,
                        JaxRsActivator.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addClass(Resources.class)
                .merge(AUTHENTICATION()).merge(PERSISTENCE());
    }

    @Test
    public void testReset() throws Exception {

        client(ManagementResource.class, SANTA_CLAUS_NO, "secret").reset();

    }
}
