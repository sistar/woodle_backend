package woodle.backend.test.auth;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.protocol.servlet.arq514hack.descriptors.api.web.WebAppDescriptor;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)

public class AuthTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebAppDescriptor webXml = Descriptors.create(WebAppDescriptor.class);
        return ShrinkWrap.create(WebArchive.class, "basic.war")

                .addAsWebResource("basic/index.xhtml", "index.xhtml")
                .addAsWebInfResource("common/faces-config.xml", "faces-config.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")  ;
                /*.setWebXML(new StringAsset(webXml.getOrCreateContextParam()
                        .paramName(ProjectStage.PROJECT_STAGE_PARAM_NAME).paramValue(ProjectStage.Development.name()).up()
                        .exportAsString()));    */
    }

}
