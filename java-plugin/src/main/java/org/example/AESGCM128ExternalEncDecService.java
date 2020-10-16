package org.example;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class AESGCM128ExternalEncDecService {
    private static  String cipherTransformation = "AES/GCM/NoPadding";
    private static final String aesEncryptionAlgorithm = "AES";
    private static final int GCM_TAG_LENGTH = 16;

    private String key;

    private String iv;

    public AESGCM128ExternalEncDecService(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    public String encrypt(String data) throws Exception{
        String ivStr = iv;
        if(ivStr == null || ivStr.isEmpty()){
            ivStr = getRandomString(16);
        }

        byte[] key = Base64.getDecoder().decode(this.key);
        return encrypt(data.getBytes(), key, ivStr.getBytes());
    }

    public String  decrypt(String data) throws Exception{
        byte[] valueBytes = Base64.getDecoder().decode(data.getBytes());
        byte[] decodedKey = Base64.getDecoder().decode(key);
        byte[] encryptedText = Arrays.copyOfRange(valueBytes, 0, valueBytes.length - 18);
        byte[] iv = Arrays.copyOfRange(valueBytes,valueBytes.length-16, valueBytes.length);
        return decrypt(encryptedText,decodedKey,iv);
    }


    public static String encrypt(byte[] plainText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,0,16, aesEncryptionAlgorithm);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, initialVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
        plainText = cipher.doFinal(plainText);
        String encText = new String (Base64.getEncoder().encode(plainText))+"::"+new String(initialVector);
        return new String (Base64.getEncoder().encode(encText.getBytes()));
    }

    public  static String decrypt(byte[] cipherText, byte[] key, byte [] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        //Added
        cipherText = Base64.getDecoder().decode(cipherText);
        Cipher cipher = Cipher.getInstance(cipherTransformation);
        SecretKeySpec secretKeySpecy = new SecretKeySpec(key,0,16, aesEncryptionAlgorithm);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, initialVector);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, gcmParameterSpec);
        cipherText = cipher.doFinal(cipherText);
        return new String(cipherText);
    }


    public static String getRandomString(int length){
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rng = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        while(length > 0){
            length --;
            sb.append(ALPHABET.charAt(rng.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

}
