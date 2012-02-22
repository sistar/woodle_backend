package woodle.backend.test.persistence;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import woodle.backend.controller.MemberRegistration;
import woodle.backend.entity.Principle;
import woodle.backend.model.Member;
import woodle.backend.util.Resources;

import javax.inject.Inject;
import java.util.logging.Logger;

@Ignore(value = "persistence not implemented")
@RunWith(Arquillian.class)
public class MemberRegistrationTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Member.class, MemberRegistration.class, Resources.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    MemberRegistration memberRegistration;

    @Inject
    Logger log;

    @Test
    public void testRegister() throws Exception {
        memberRegistration.register(new Principle("jane@mailinator.com", ("topSecret")));

    }

}
