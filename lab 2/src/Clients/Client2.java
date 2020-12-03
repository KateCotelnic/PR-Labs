package Clients;

import Protocols.Session.SecurityProtocol;
import Protocols.Transport.TransportProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) {
        try {
            int port;
            InetAddress address;
            DatagramSocket socket = new DatagramSocket(3001);
            byte[] bytes = new byte[1000];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            socket.receive(packet);
            address = packet.getAddress();
            port = packet.getPort();
            String backMessage = new String(bytes, 0, packet.getLength());
            System.out.println("Receive: " + backMessage);
            SecurityProtocol securityProtocol = new SecurityProtocol(true,socket,address,port);
//            securityProtocol.send("answering");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
