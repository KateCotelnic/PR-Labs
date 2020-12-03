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

public class Client1 {
    public static void main(String[] args){
        try {
            int port = 3000;
            InetAddress address = InetAddress.getLocalHost();
            DatagramSocket socket = new DatagramSocket();

//            Scanner scanner = new Scanner(System.in);
            String reader;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

//            TransportProtocol transportProtocol = new TransportProtocol(socket,address,port);
            /////////////first send "took the phone"//////////////
            while (true) {
                System.out.println("Enter the command: ");
//                reader = scanner.nextLine();
                reader = in.readLine();

                byte[] buffer = reader.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
//                transportProtocol.send(reader);
                if(reader.equals("took"))
                    break;
            }
            //////////////////receive public key//////////////////
//                if(reader.equals("took")){
//            System.out.println("began");
            SecurityProtocol securityProtocol = new SecurityProtocol(true,socket,address,port);
            while (true) {
                System.out.println("Enter the number: ");
                reader = in.readLine();
                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < 3 * 1000
                        && !in.ready()) {
                }
                securityProtocol.send(reader);
                if (in.ready()) {
//                    System.out.println("here");
//                    securityProtocol.send("call");
                } else {
                    System.out.println("time out");
                    securityProtocol.send("call");
                    break;
                }
            }
            SecurityProtocol securityProtocol1 = new SecurityProtocol(false, socket, address, port);
//            String answer = securityProtocol1.receive();
//            System.out.println("answer = " + answer);
//            String s = securityProtocol.receive();
//            System.out.println(s);

//            String key = transportProtocol.receive();
//            System.out.println(key);

//                    SecurityProtocol securityProtocol = new SecurityProtocol(transportProtocol,true,socket,address,port);
//                }
//                transportProtocol.send(reader);
//            }

//            String s = transportProtocol.receive(socket,address);
//            System.out.println(s);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        try (Socket socket = new Socket("localhost",3000)){
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            PrintWriter printer = new PrintWriter(socket.getOutputStream(),true);
//
//            Scanner scanner = new Scanner(System.in);
//            String string;
//            String response;
//            do{
//                System.out.println("\nAsk to send: ");
//                string = scanner.nextLine();
//                printer.println(string);
//                if(!string.equals("")){
//                    response = reader.readLine();
//                    System.out.println(response);
//                }
//            } while (!string.equals(""));
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.setProperty("javax.net.ssl.trustStore", "za.store");
//        try (Socket socket = (SSLSocketFactory.getDefault()).createSocket("localhost", 3000)){
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter printer = new PrintWriter(socket.getOutputStream(),true);
//            BufferedReader commandPrompReader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.print("Enter your username: ");
//            printer.println(commandPrompReader.readLine());
//            String message = null;
//            while (true){
//                System.out.println("please enter a message: ");
//                message = commandPrompReader.readLine();
//                if(message.equals("")) {
//                    socket.close();
//                    break;
//                }
//            }
//            printer.println(message);
//            System.out.println("message from server: ");
//            System.out.println(reader.readLine());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
