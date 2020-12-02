package Protocols.Session;

import Protocols.Transport.TransportProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;

public class SecurityProtocol {
    //    private String message;
    private BigInteger public_modulus;
    private BigInteger private_modulus;
    private  BigInteger public_exponent;
    private BigInteger private_exponent;
    private TransportProtocol transportProtocol;

    public SecurityProtocol(){

    }
    public SecurityProtocol(boolean want_to_send, DatagramSocket socket, InetAddress address, int port) throws NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InterruptedException, IOException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        this.transportProtocol = new TransportProtocol(socket,address,port);
        if(!want_to_send) {
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
            send_key();
        }
        else{
//            transportProtocol.send("took");
//            System.out.println("here");
            receive_key();
        }
    }

    private void send_key() throws IllegalBlockSizeException, InterruptedException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        String public_key = public_exponent + " " + public_modulus;
        System.out.println("pm = " + public_modulus);
        System.out.println("pe = " + public_exponent);
        transportProtocol.send(public_key);
    }

    private void receive_key() throws InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String key = transportProtocol.receive();
//        System.out.println("key = " + key);
        String[] arrOfStr = key.split(" ", 5);
        public_exponent = new BigInteger(arrOfStr[0]);
        System.out.println("\ngot public modulus: " + public_exponent);
//        int i = 0;
//        while (arrOfStr[1].charAt(i) >= '0' && arrOfStr[1].charAt(i) <= '9')
//            i++;
//        String s = arrOfStr[1].substring(0,i);
        public_modulus = new BigInteger(arrOfStr[1]);
        System.out.println("\ngot public exponent: " + public_modulus);
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

    public static void main(String[] args) {
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);

            SecurityProtocol rsaObj = new SecurityProtocol();
//            rsaObj.saveKeys(publicKeyFile, rsaPublicKeySpec.getModulus(), rsaPublicKeySpec.getPublicExponent());
//            rsaObj.saveKeys(privateKeyFile, rsaPrivateKeySpec.getModulus(), rsaPrivateKeySpec.getPrivateExponent());

            rsaObj.public_modulus = rsaPublicKeySpec.getModulus();
            rsaObj.public_exponent = rsaPublicKeySpec.getPublicExponent();
            rsaObj.private_modulus = rsaPrivateKeySpec.getModulus();
            rsaObj.private_exponent = rsaPrivateKeySpec.getPrivateExponent();

            byte[] encryptedData = rsaObj.encryptData("5",rsaObj.getPublic_exponent(), rsaObj.getPublic_modulus());
            String data = new String(encryptedData, 0, encryptedData.length);
            System.out.println("data = " + data);

            String decreptedData = rsaObj.decryptData(data.getBytes(), rsaObj.private_exponent, rsaObj.private_modulus);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encryptData(String data, BigInteger public_exponent, BigInteger public_modulus) throws NoSuchAlgorithmException {
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

    public static String decryptData(byte[] data, BigInteger private_exponent, BigInteger private_modulus){
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


    public void send(String data) throws IllegalBlockSizeException, InterruptedException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
//        transportProtocol.setSecured(true);
//        String encrypted_data = encryptData(data);
//        System.out.println("encr = " + encrypted_data);
//        String decrypted_data = decryptData(encrypted_data);
//        System.out.println("decr = " + decrypted_data);
        transportProtocol.send(data,public_exponent,public_modulus);
    }

    public String receive() throws InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {
//        transportProtocol.setSecured(true);
        String data = transportProtocol.receive(private_exponent,private_modulus);
//        String encrypted_data = encryptData(data);
//        System.out.println("encr = " + encrypted_data);
//        String decrypted_data = decryptData(encrypted_data);
//        System.out.println("decr = " + decrypted_data);
        return data;
    }
}
