package Protocols.Session;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class SecurityProtocol {
    //    private String message;
    private BigInteger public_modulus;
    private BigInteger private_modulus;
    private  BigInteger public_exponent;
    private BigInteger private_exponent;

    public SecurityProtocol() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);

//            RSAEncryption rsaObj = new RSAEncryption();
//            rsaObj.saveKeys(publicKeyFile, rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent());
//            rsaObj.saveKeys(privateKeyFile, rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent());

            this.public_modulus = rsaPublicKeySpec.getModulus();
            this.public_exponent = rsaPublicKeySpec.getPublicExponent();
            private_modulus = rsaPrivateKeySpec.getModulus();
            private_exponent = rsaPrivateKeySpec.getPrivateExponent();

//            byte[] encryptedData = rsaObj.encryptData("Data to encrypt");

//            String decreptedData = rsaObj.decryptData(encryptedData,private_modulus, private_exponent);

    }

    public SecurityProtocol(BigInteger public_modulus, BigInteger public_exponent){
        this.public_modulus = public_modulus;
        this.public_exponent = public_exponent;
    }

    public BigInteger getPublic_modulus() {
        return public_modulus;
    }

    public void setPublic_modulus(BigInteger public_modulus) {
        this.public_modulus = public_modulus;
    }

    public BigInteger getPrivate_modulus() {
        return private_modulus;
    }

    public void setPrivate_modulus(BigInteger private_modulus) {
        this.private_modulus = private_modulus;
    }

    public BigInteger getPublic_exponent() {
        return public_exponent;
    }

    public void setPublic_exponent(BigInteger public_exponent) {
        this.public_exponent = public_exponent;
    }

    public BigInteger getPrivate_exponent() {
        return private_exponent;
    }

    public void setPrivate_exponent(BigInteger private_exponent) {
        this.private_exponent = private_exponent;
    }

//    public void main(String[] args) {
//        try{
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            PublicKey publicKey = keyPair.getPublic();
//            PrivateKey privateKey = keyPair.getPrivate();
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
//            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
//
//            RSAEncryption rsaObj = new RSAEncryption();
////            rsaObj.saveKeys(publicKeyFile, rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent());
////            rsaObj.saveKeys(privateKeyFile, rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent());
//
//            this.public_modulus = rsaPublicKeySpec.getModulus();
//            this.public_exponent = rsaPublicKeySpec.getPublicExponent();
//            private_modulus = rsaPrivateKeySpec.getModulus();
//            private_exponent = rsaPrivateKeySpec.getPrivateExponent();
//
//            byte[] encryptedData = rsaObj.encryptData("Data to encrypt");
//
//            String decreptedData = rsaObj.decryptData(encryptedData,private_modulus, private_exponent);
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        }
//    }

    public byte[] encryptData(String data) throws NoSuchAlgorithmException {
        System.out.println("\nData before encr: " + data);
        byte[] dataToEncrypt = data.getBytes();
        byte[] encyptedData = null;
        try {
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(public_modulus, public_exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(rsaPublicKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            encyptedData = cipher.doFinal(dataToEncrypt);
            System.out.println("\nEncrypted Data: " + encyptedData);
        } catch (InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encyptedData;
    }

    public String decryptData(byte[] data){
        System.out.println("\nData before decr: " + new String(data));
        byte[] decryptedData = null;
        String decData = "";
        try{
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(private_modulus, private_exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(rsaPrivateKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedData = cipher.doFinal(data);
            decData = new String(decryptedData);
            System.out.println("\nDecrypted Data: " + decData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return decData;
    }
//    private void saveKeys(String fileName, BigInteger mod, BigInteger exp) throws IOException{
//        FileOutputStream fos
//    }


}
