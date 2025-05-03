package io.beanvortex.bitkip.model;

import io.beanvortex.bitkip.models.Credentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CredentialsTest {
    @Test
    public void testCredentials() {
        Credentials c = new Credentials(null, null);
        Assertions.assertFalse(c.isOk());
        Assertions.assertNull(Credentials.decrypt(null));
        c = new Credentials("", null);
        Assertions.assertFalse(c.isOk());
        c = new Credentials("", "");
        Assertions.assertFalse(c.isOk());
        c = new Credentials("test", "");
        Assertions.assertFalse(c.isOk());
        c = new Credentials("test", "test");
        Assertions.assertTrue(c.isOk());
        Assertions.assertNotNull(c.base64Encoded());
        Assertions.assertNotNull(c.encrypt());
        String encrypt = c.encrypt();
        Assertions.assertNotNull(Credentials.decrypt(encrypt));

    }
}
