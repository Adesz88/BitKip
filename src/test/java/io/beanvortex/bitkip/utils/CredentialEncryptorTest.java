package io.beanvortex.bitkip.utils;

import io.beanvortex.bitkip.config.AppConfigs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CredentialEncryptorTest {

    @BeforeEach
    public void setup() {
        AppConfigs.initPaths();
        AppConfigs.initLogger();
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        // Test data
        String originalText = "testPassword123";
        
        // Encrypt
        String encryptedText = CredentialEncryptor.encrypt(originalText);
        Assertions.assertNotNull(encryptedText);
        Assertions.assertNotEquals(originalText, encryptedText);
        
        // Decrypt
        String decryptedText = CredentialEncryptor.decrypt(encryptedText);
        Assertions.assertEquals(originalText, decryptedText);
    }

    @Test
    public void testEncryptWithEmptyString() throws Exception {
        String originalText = "";
        
        // Encrypt
        String encryptedText = CredentialEncryptor.encrypt(originalText);
        Assertions.assertNotNull(encryptedText);
        
        // Decrypt
        String decryptedText = CredentialEncryptor.decrypt(encryptedText);
        Assertions.assertEquals(originalText, decryptedText);
    }
}
