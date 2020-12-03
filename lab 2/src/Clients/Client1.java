package Clients;

import Protocols.Session.SecurityProtocol;

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
import java.util.concurrent.TimeUnit;

public class Client1 {
    public static void main(String[] args){
        try {
            int port = 3000;
            InetAddress address = InetAddress.getLocalHost();
            DatagramSocket socket = new DatagramSocket();
            String reader;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Enter the command: ");
                reader = in.readLine();

                byte[] buffer = reader.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                if(reader.equals("took"))
                    break;
            }
            SecurityProtocol securityProtocol = new SecurityProtocol(true,socket,address,port);
            while (true) {
                System.out.println("Enter the number: ");
                ConsoleInput con = new ConsoleInput(
                        1,
                        4,
                        TimeUnit.SECONDS
                );

                reader = con.readLine();
                if(reader == null){
                    securityProtocol.send("call");
                    break;
                }
                else {
                    securityProtocol.send(reader);
                }
            }
            SecurityProtocol securityProtocol1 = new SecurityProtocol(false, socket, address, port);
            String answer = securityProtocol1.receive();
            System.out.println("answer = " + answer);
            if(answer.equals("is busy") || answer.equals("no answer") || answer.equals("number has not been found")){

            }else {
                System.out.println(answer);
                int port1 = Integer.parseInt(answer);
                InetAddress address1 = InetAddress.getLocalHost();
                String str = "Connection";
                byte[] buffer = str.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address1, port1);
                socket.send(packet);
                SecurityProtocol securityProtocolReceiver = new SecurityProtocol(false,socket,address1,port1);
                SecurityProtocol securityProtocolSender = new SecurityProtocol(true,socket,address1,port1);
                while (true){
                    String s = securityProtocolReceiver.receive();
                    System.out.println("answer: " + s);
                    reader = in.readLine();
                    securityProtocolSender.send(reader);
                    if(s.equals("bye"))
                        break;
                }
            }
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
    }
}
