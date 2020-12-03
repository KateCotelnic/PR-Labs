package Server;

import Protocols.Session.SecurityProtocol;
import Protocols.Transport.TransportProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Server {

    private static String[][] numbers = {{"1", "12"},{"2","068742396"}};

    public static void main(String[] args) throws IOException {
        try {
            int port = 3000;
            DatagramSocket socket = new DatagramSocket(port);
            InetAddress address;
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
            String number = ""; boolean is_number = true;
            while (true) {
                backMessage = securityProtocol.receive();
                System.out.println("Receive: " + backMessage);
                if(backMessage.equals("call")){
                    break;
                }
                if(backMessage.length()>1)
                    is_number = false;
                number +=backMessage;
            }
            System.out.println("number = " + number);
            if (!number.matches("[0-9]+") || number.length() < 2 || number.length() > 15) {
                is_number = false;
            }
            SecurityProtocol securityProtocol1 = new SecurityProtocol(true,socket,address,port);
            BigInteger public_exponent = securityProtocol1.getPublic_exponent();
            BigInteger public_modulus = securityProtocol1.getPublic_modulus();
            System.out.println("public exponent = " + public_exponent);
            System.out.println("public modulus = " + public_modulus);
            if(is_number){
                if(number.equals(numbers[0][1]) || number.equals(numbers[1][1])){
                    DatagramSocket socket1 = new DatagramSocket();
                    System.out.println("true");
                    int port1 = 3001;
                    InetAddress address1 = InetAddress.getLocalHost();
                    String str = "Phone is calling";
                    byte[] buffer = str.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address1, port1);
                    socket.send(packet);
                    SecurityProtocol securityProtocol2 = new SecurityProtocol(false,socket1,address1,port1);
//                    String answer = securityProtocol2.receive();
//                    System.out.println("answer = " + answer);
                }
                else {
                    System.out.println("no such number");
                    System.out.println("address = " + address.toString());
                    System.out.println("port = " + port);
                    securityProtocol1.send("number has not been found");
                }
            }
            else {
                System.out.println("wrong input");
                securityProtocol1.send("wrong input");
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