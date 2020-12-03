package Server;

import Protocols.Session.SecurityProtocol;

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

    private static String[][] numbers = {{"1", "022568932"},{"2","022578925"}};

    public static void main(String[] args){
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
                if(backMessage.equals("took")){
                    break;
                }
            }
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
                    System.out.println("true");
                    int port1 = 3001;
                    InetAddress address1 = InetAddress.getLocalHost();
                    String str = "Phone is calling";
                    byte[] buffer = str.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address1, port1);
                    socket.send(packet);
                    SecurityProtocol securityProtocol2 = new SecurityProtocol(false,socket,address1,port1);
                    String answer = securityProtocol2.receive();
                    System.out.println("answer = " + answer);
                    if(answer.equals("busy")){
                        securityProtocol1.send("is busy");
                    }
                    else if(answer.equals("no answer")){
                        securityProtocol1.send("no answer");
                    }
                    else {
                        securityProtocol1.send(answer);
                    }
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

        } catch (SocketException e) {
        } catch (IOException e) {
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
    }
}