package Protocols.Transport;

import Protocols.Session.SecurityProtocol;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class Sender implements Runnable{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private String message;
    private boolean secured;
    private BigInteger exponent;
    private BigInteger modulus;

    public Sender(DatagramSocket socket, InetAddress address, int port, String message, boolean secured, BigInteger exponent, BigInteger modulus){
        this.socket = socket;
        this.address = address;
        this.port = port;
        this.message = message;
        this.secured = secured;
        this.exponent = exponent;
        this.modulus = modulus;
    }

    @Override
    public void run() {
        byte[] bytes = message.getBytes();
        if(secured){
            try {
                bytes = SecurityProtocol.encryptData(message,exponent,modulus);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        try {
            socket.send(packet);
            System.out.println("\nsend: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
