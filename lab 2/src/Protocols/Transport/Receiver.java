package Protocols.Transport;

//import Protocols.Session.SecurityProtocol;

import Protocols.Session.SecurityProtocol;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Receiver implements Runnable{
    private String data = "";
    private List<Integer> orders = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private DatagramSocket socket;
    private volatile boolean killed = false;
    public static boolean is_ok = true;
    public int n = 0;
    public static int attempts = 5;
//    private SecurityProtocol securityProtocol;

    public Receiver(DatagramSocket socket) {
        this.orders = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.socket = socket;
//        this.securityProtocol = securityProtocol;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public void run() {
//        System.out.println("orders: " + orders.toString());
//        System.out.println("messages: " + messages.toString());
//        if(!is_ok){
//            kill();
//        }
//        while (!killed) {
//            try {
//                doOnce();
                n++;
//            }
//            catch (IOException ex)
//            { killed = true; }
//        }
    }

    public void kill() { killed = true; }

    public void doOnce(boolean secured, BigInteger exponent, BigInteger modulus) throws IOException {
        byte[] bytes = new byte[1000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
//        for(int i = 0; i < bytes.length;i++){
//            System.out.print(bytes[i] + ", ");
//        }
        int lastGoodChar=0;
        while (!(bytes[lastGoodChar]==0 && bytes[lastGoodChar+1]==0 && bytes[lastGoodChar+2]==0))
            lastGoodChar++;
//        System.out.println("last = " + lastGoodChar);
//        System.out.println(bytes[lastGoodChar-1]);
        byte[] buf = new byte[lastGoodChar];
//        buf = bytes;
        for(int i = 0; i < lastGoodChar;i++){
            buf[i] = bytes[i];
        }
//        for(int i = 0; i < buf.length;i++){
//            System.out.print(buf[i] + ", ");
//        }
//        System.out.println("115");
        String tmp;
        if(secured){
            tmp = SecurityProtocol.decryptData(buf,exponent,modulus);
        }
        else {
            tmp = new String(buf, 0, packet.getLength());
        }
        System.out.println("\nGot: " + tmp);
//        String key = "l5ukpxmtg7u8ko/t";
//        SecurityProtocol securityProtocol = new SecurityProtocol(tmp);
//        tmp = securityProtocol.getMessage();

//        tmp = securityProtocol.getKeys().decryptData(tmp.getBytes(),securityProtocol.getKeys().getPrivate_modulus(), securityProtocol.getKeys().getPrivate_exponent());
        String order = "";
        String receivedMessage = "";
        String receivedChecksum = "";
        int i = 0;
        while (tmp.charAt(i) != ' '){
            order += tmp.charAt(i);
            i++;
        }
//        System.out.println("Order : " + order);
        i++;
        while (tmp.charAt(i) != '/'){
            receivedMessage += tmp.charAt(i);
            i++;
        }
        i++;
        while (tmp.charAt(i)!= '*'){
            receivedChecksum += tmp.charAt(i);
            i++;
        }
        byte[] bytesOfMessage = receivedMessage.getBytes();
        String newChecksum = TransportProtocol.getCRC32Checksum(bytesOfMessage) + "";
//        newChecksum = "fe";
        if(!newChecksum.equals(receivedChecksum)){
//            System.out.println(newChecksum + " " + receivedChecksum);
            is_ok = false;
        }
//        else if(receivedMessage.equals("call")){
//            is_ok = false;
//        }
        else {
//            System.out.println("n = "+n);
//            if(n==0){
//                orders = new ArrayList<>();
//            }
//            System.out.println("orders: " + orders.toString());
//            System.out.println("\nOrder " + Integer.parseInt(order));
            orders.add(Integer.parseInt(order));
//            System.out.println("orders: " + orders.toString());
            messages.add(receivedMessage);
//            System.out.println("\nMessage " + receivedMessage);
//            Thread.sleep(10);
        }
    }
    public int get(boolean secured, BigInteger exponent, BigInteger modulus) throws IOException {
        byte[] bytes = new byte[1000];
//        System.out.println("113");
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
//        System.out.println("bytes: ");
//        for(int i = 0; i < bytes.length;i++){
//            System.out.print(bytes[i] + ", ");
//        }
//        System.out.println();
        int lastGoodChar=0;
        while (!(bytes[lastGoodChar]==0 && bytes[lastGoodChar+1]==0 && bytes[lastGoodChar+2]==0))
            lastGoodChar++;
//        System.out.println("last = " + lastGoodChar);
//        System.out.println(bytes[lastGoodChar-1]);
        byte[] buf = new byte[lastGoodChar];
//        buf = bytes;
        for(int i = 0; i < lastGoodChar;i++){
            buf[i] = bytes[i];
        }
//        for(int i = 0; i < buf.length;i++){
//            System.out.print(buf[i] + ", ");
//        }
//        System.out.println("115");
        String tmp;
        if(secured){
            tmp = SecurityProtocol.decryptData(buf,exponent,modulus);
        } else{
            tmp = new String(bytes, 0, packet.getLength());
        }
        System.out.println("\nGot: " + tmp);
//        String key = "l5ukpxmtg7u8ko/t";
//        SecurityProtocol securityProtocol = new SecurityProtocol(tmp);
//        tmp = securityProtocol.getMessage();

//        tmp = securityProtocol.getKeys().decryptData(tmp.getBytes(),securityProtocol.getKeys().getPrivate_modulus(), securityProtocol.getKeys().getPrivate_exponent());
        String order = "";
        String receivedMessage = "";
        String receivedChecksum = "";
        int i = 0;
        while (tmp.charAt(i) != ' '){
            order += tmp.charAt(i);
            i++;
        }
//        System.out.println("Order : " + order);
        i++;
        while (tmp.charAt(i) != '/'){
            receivedMessage += tmp.charAt(i);
            i++;
        }
        i++;
        while (tmp.charAt(i)!= '*'){
            receivedChecksum += tmp.charAt(i);
            i++;
        }
        byte[] bytesOfMessage = receivedMessage.getBytes();
        String newChecksum = TransportProtocol.getCRC32Checksum(bytesOfMessage) + "";
//        newChecksum = "fe";
        if(!newChecksum.equals(receivedChecksum)){
//            System.out.println(newChecksum + " " + receivedChecksum);
            return -1;
        }
//        else if(receivedMessage.equals("call")){
//            is_ok = false;
//        }
//        else {
//            System.out.println("n = "+n);
//            if(n==0){
//                orders = new ArrayList<>();
//            }
            return Integer.parseInt(receivedMessage);
//            System.out.println("orders: " + orders.toString());
//            System.out.println("\nOrder " + Integer.parseInt(order));
//            orders.add(Integer.parseInt(order));
//            System.out.println("orders: " + orders.toString());
//            messages.add(receivedMessage);
//            System.out.println("\nMessage " + receivedMessage);
//            Thread.sleep(10);
//        }
//        return receivedMessage;
    }
}
