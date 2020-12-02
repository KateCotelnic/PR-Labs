package Protocols.Transport;

//import Protocols.Session.SecurityProtocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class Sender implements Runnable{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private String message;

    public Sender(DatagramSocket socket, InetAddress address, int port, String message) throws NoSuchAlgorithmException {
        this.socket = socket;
        this.address = address;
        this.port = port;
        this.message = message;
//        this.message = new String(securityProtocol.getKeys().encryptData(message));
    }

    @Override
    public void run() {
//        System.out.println(message);
        byte[] bytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nsend: " + message);
    }
}
