package org.chat.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RSAUtil {

    public static final String ALGORITHM = "RSA";
    public static final String SHA_256_WITH_RSA = "SHA256withRSA";

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance(ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance(ALGORITHM);
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance(SHA_256_WITH_RSA);
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance(SHA_256_WITH_RSA);
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    public static PublicKey getPublicKey(String key){
        try{
            byte[] byteKey = Base64.getDecoder().decode(key);
            return getPublicKey(byteKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(encodedKey);
        return factory.generatePublic(encodedKeySpec);
    }

    public static String convertPublicKeyToString(PublicKey key) throws NoSuchAlgorithmException {
        byte[] rawData = key.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    public static String getMD5(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pass.getBytes());
        byte[] digest = md.digest();
        String str = Base64.getEncoder().encodeToString(digest);
        return str;
    }
}
