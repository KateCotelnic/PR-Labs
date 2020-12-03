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

public class SecurityProtocol {
    private BigInteger public_modulus;
    private BigInteger private_modulus;
    private  BigInteger public_exponent;
    private BigInteger private_exponent;
    private TransportProtocol transportProtocol;

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

            this.public_modulus = rsaPublicKeySpec.getModulus();
            this.public_exponent = rsaPublicKeySpec.getPublicExponent();
            private_modulus = rsaPrivateKeySpec.getModulus();
            private_exponent = rsaPrivateKeySpec.getPrivateExponent();

            send_key();
        }
        else{
            receive_key();
        }
    }

    private void send_key() throws InterruptedException, NoSuchAlgorithmException{
        String public_key = public_exponent + " " + public_modulus;
        transportProtocol.send(public_key);
    }

    private void receive_key() throws InterruptedException, IOException {
        String key = transportProtocol.receive();
        String[] arrOfStr = key.split(" ", 5);
        public_exponent = new BigInteger(arrOfStr[0]);
        System.out.println("\ngot public modulus: " + public_exponent);
        public_modulus = new BigInteger(arrOfStr[1]);
        System.out.println("\ngot public exponent: " + public_modulus);
    }

    public BigInteger getPublic_modulus() {
        return public_modulus;
    }

    public BigInteger getPublic_exponent() {
        return public_exponent;
    }


    public static byte[] encryptData(String data, BigInteger public_exponent, BigInteger public_modulus) throws NoSuchAlgorithmException {

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
        System.out.println("\nReceived data: " + new String(data));
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


    public void send(String data) throws InterruptedException, NoSuchAlgorithmException {

        transportProtocol.send(data,public_exponent,public_modulus);
    }

    public String receive() throws InterruptedException,IOException {
        String data = transportProtocol.receive(private_exponent,private_modulus);
        return data;
    }
}
