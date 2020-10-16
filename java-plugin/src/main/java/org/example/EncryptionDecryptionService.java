package org.example;

public interface EncryptionDecryptionService {
    String encrypt(String message) throws Exception;

    String decrypt(String message) throws Exception;
}
