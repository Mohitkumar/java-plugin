package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception{
        EncryptionDecryptionService encryptionDecryptionService = EncryptionDecryptionServiceFactory.getEncryptionDecryptionService("org.example.AESGCM128ExternalEncDecService", "buPCKYpNdEVNc3pYc0owVA==", null);
        String data = encryptionDecryptionService.encrypt("data");
        System.out.println(data);
        System.out.println(encryptionDecryptionService.decrypt(data));
    }
}
