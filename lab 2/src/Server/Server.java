package Server;

import Protocols.Transport.TransportProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Server {

    public static void main(String[] args) throws IOException {
        try{
            int port = 3000;
            DatagramSocket socket = new DatagramSocket(port);
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Server is up");
            TransportProtocol transportProtocol = new TransportProtocol();
            while (true){
                byte[] bytes = new byte[1000];
                DatagramPacket packet = new DatagramPacket(bytes,bytes.length);
                socket.receive(packet);
                String backMessage = new String(bytes,0,packet.getLength());
                System.out.println("Receive: " + backMessage);
//                TransportProtocol transportProtocol = new TransportProtocol(socket,address);
                String returnString ="hi!my name is kate! I love very much Cat, mom, Polly and my pretty dog Rudy. Bye!";
                transportProtocol.send(socket, packet.getAddress(), packet.getPort(),returnString);
            }
        } catch (SocketException e){
        } catch (IOException e){
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
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

//        System.setProperty("javax.net.ssl.keyStore", "kk.store");
//        System.setProperty("javax.net.ssl.keyPassword", "kpassword");
//        ServerSocket serverSocket = ((SSLServerSocketFactory) SSLServerSocketFactory.getDefault()).createServerSocket(3000);
//        System.out.println("Server up");
//        while (true){
//            new ServerThread(serverSocket.accept()).start();
//        }
//    }
    }
}