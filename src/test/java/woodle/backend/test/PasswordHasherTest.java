package woodle.backend.test;

import org.junit.Assert;
import org.junit.Test;
import woodle.backend.data.WoodleStore;

public class PasswordHasherTest {
    @Test
    public void testThatPasswordGetsHashedAndEncoded() throws Exception {
        String secretHashed = WoodleStore.sha256Base64("secret");
        Assert.assertEquals("K7gNU3sdo+OL0wNhqoVWhr3g6s1xYv72ol/pe/Unols=", secretHashed);
    }
}
