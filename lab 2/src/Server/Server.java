package Server;

import Protocols.Session.SecurityProtocol;
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
        try {
            int port = 3000;
            DatagramSocket socket = new DatagramSocket(port);
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Server is up");
            String backMessage = "";
            while (true) {
                byte[] bytes = new byte[1000];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                socket.receive(packet);
                address = packet.getAddress();
                port = packet.getPort();
                backMessage = new String(bytes, 0, packet.getLength());
                System.out.println("Receive: " + backMessage);
//                String tmp = transportProtocol.receive();
//            System.out.println(tmp);
                if(backMessage.equals("took")){
                    break;
                }
//                String s = transportProtocol.receive(socket, address);
//                System.out.println("s = " + s);
//                if (s.equals("call"))
//                    break;
//                if (!s.isEmpty()) {
//                    string += s;
//                }
            }
//            TransportProtocol transportProtocol = new TransportProtocol(socket,address,port);
//            Thread.sleep(1000);
            System.out.println("began");
            SecurityProtocol securityProtocol = new SecurityProtocol(false,socket,address,port);
            while (true) {
                backMessage = securityProtocol.receive();
                System.out.println("Receive: " + backMessage);
                if(backMessage.equals("call")){
                    break;
                }
            }
//            System.out.println("string = " + string);
//            Thread.sleep(1500);
//            transportProtocol.send(socket,address,port,string + " hi! thats me)");

        } catch (SocketException e) {
        } catch (IOException e) {
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }  catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
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