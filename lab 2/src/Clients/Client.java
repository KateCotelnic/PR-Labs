package Clients;

import Protocols.Transport.TransportProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        try {
            int port = 3000;
            InetAddress address = InetAddress.getLocalHost();
            DatagramSocket socket = new DatagramSocket();

            Scanner scanner = new Scanner(System.in);
            String reader;
            TransportProtocol transportProtocol = new TransportProtocol();
            do{
                System.out.println("Enter the number: ");
                reader = scanner.nextLine();
                transportProtocol.send(socket, address, port, reader);
            }
            while (!reader.equals("call"));

//            String s = transportProtocol.receive(socket,address);
//            System.out.println(s);

        } catch (SocketTimeoutException e){
        } catch (IOException e){
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
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
