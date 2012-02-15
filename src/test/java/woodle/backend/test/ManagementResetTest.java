package woodle.backend.test;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ProxyFactory;
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

import javax.ws.rs.ApplicationPath;
import java.net.URL;

@RunWith(Arquillian.class)
public class ManagementResetTest {
    private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);

    @Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "woodle_backend.war")
                .addPackage("woodle.backend.model")
                .addClasses(ManagementResource.class,
                        ManagementResourceService.class,
                        WoodleStore.class,
                        JaxRsActivator.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testPutAppointmentUsingClientProxy() throws Exception {

        String path = deploymentUrl.toString() + RESOURCE_PREFIX;
        ManagementResource managementResource = ProxyFactory.create(ManagementResource.class,
                path);
        managementResource.reset();

    }
}
