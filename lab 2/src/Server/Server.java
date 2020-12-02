package Server;

import Protocols.Transport.TransportProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
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
            TransportProtocol transportProtocol = new TransportProtocol();
            String string = "";
            while (true) {
                String s = transportProtocol.receive(socket, address);
                System.out.println("s = " + s);
                if (s.equals("call"))
                    break;
                if (!s.isEmpty()) {
                    string += s;
                }
            }
            System.out.println("string = " + string);
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