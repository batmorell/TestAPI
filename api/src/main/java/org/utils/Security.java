package org.utils;



import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import config.Config;

public class Security {

    public static String md5(String string) throws TechniqueException {
        return hash("MD5", string);
    }

    public static String md5(String string, int length) throws TechniqueException {
        return hash("MD5", string, length);
    }

    public static String saltedSHA1(String string) throws TechniqueException {
        return hash("SHA1", string + Config.SALT);
    }

    public static String sha1(String string) throws TechniqueException {
        return hash("SHA1", string);
    }

    public static String sha1(String string, int length) throws TechniqueException {
        return hash("SHA1", string, length);
    }

    private static String hash(String algorithm, String string) throws TechniqueException {
        if (string == null) {
            return null;
        }

        byte[] uniqueKey;
        try {
            uniqueKey = string.getBytes("UTF-8");
            byte[] hash;
            try {
                hash = MessageDigest.getInstance(algorithm).digest(uniqueKey);
            } catch (NoSuchAlgorithmException e) {
                throw new TechniqueException("No " + algorithm + " support in this VM.");
            }
            StringBuilder hashString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() == 1) {
                    hashString.append('0');
                    hashString.append(hex.charAt(hex.length() - 1));
                } else
                    hashString.append(hex.substring(hex.length() - 2));
            }
            return hashString.toString();
        } catch (UnsupportedEncodingException e1) {
            throw new TechniqueException("Encodage non supporté", e1);
        }
    }

    private static String hash(String algorithm, String string, int length) throws TechniqueException {
        byte[] uniqueKey;
        try {
            uniqueKey = string.getBytes("UTF-8");
            byte[] hash = new byte[length];
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(uniqueKey);
                md.digest(hash, 0, length);
            } catch (NoSuchAlgorithmException e) {
                throw new TechniqueException("No " + algorithm + " support in this VM.");
            } catch (DigestException e) {
                e.printStackTrace();
                throw new TechniqueException("An unexpected error occured during " + algorithm + " encoding.");
            }
            StringBuilder hashString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(hash[i]);
                if (hex.length() > 0) {
                    hashString.append(hex.charAt(0));
                }
            }
            return hashString.toString().toUpperCase();
        } catch (UnsupportedEncodingException e1) {
            throw new TechniqueException("Encodage non supporté", e1);
        }
    }

    public static String decrypt(String pString) {
        String lDecryptedString = "";
        try {
            byte[] lBytes = Base64.getDecoder().decode(pString);
            Key lKey = new SecretKeySpec(Config.ENCRYPT_KEY.getBytes("UTF-8"), "AES");
            Cipher lCipher = Cipher.getInstance("AES");
            lCipher.init(Cipher.DECRYPT_MODE, lKey);
            lDecryptedString = new String(lCipher.doFinal(lBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lDecryptedString;
    }

    public static String crypt(String pString) {
        String lCryptedString = "";
        try {
            Key lKey = new SecretKeySpec(Config.ENCRYPT_KEY.getBytes("UTF-8"), "AES");
            Cipher lCipher = Cipher.getInstance("AES");
            lCipher.init(Cipher.ENCRYPT_MODE,lKey);
            byte[] lBytes = lCipher.doFinal(pString.getBytes());
            lCryptedString = new String(Base64.getEncoder().encode(lBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lCryptedString;
    }
    public static void main(String[] args) throws TechniqueException {
        System.out.println(saltedSHA1("manexi"));
    }
}

