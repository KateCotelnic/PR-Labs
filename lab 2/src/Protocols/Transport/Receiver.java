package Protocols.Transport;

import Protocols.Session.SecurityProtocol;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Receiver{
    private String data = "";
    private List<Integer> orders = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private DatagramSocket socket;
    private volatile boolean killed = false;
    public static boolean is_ok = true;
    public int n = 0;
    public static int attempts = 5;

    public Receiver(DatagramSocket socket) {
        this.orders = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.socket = socket;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void doOnce(boolean secured, BigInteger exponent, BigInteger modulus) throws IOException {
        byte[] bytes = new byte[1000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        int lastGoodChar=0;
        while (!(bytes[lastGoodChar]==0 && bytes[lastGoodChar+1]==0 && bytes[lastGoodChar+2]==0))
            lastGoodChar++;
        byte[] buf = new byte[lastGoodChar];
        for(int i = 0; i < lastGoodChar;i++){
            buf[i] = bytes[i];
        }
        String tmp;
        if(secured){
            tmp = SecurityProtocol.decryptData(buf,exponent,modulus);
        }
        else {
            tmp = new String(buf, 0, packet.getLength());
        }
        System.out.println("\nGot: " + tmp);
        String order = "";
        String receivedMessage = "";
        String receivedChecksum = "";
        int i = 0;
        while (tmp.charAt(i) != ' '){
            order += tmp.charAt(i);
            i++;
        }
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
        if(!newChecksum.equals(receivedChecksum)){
            is_ok = false;
        }
        else {
            orders.add(Integer.parseInt(order));
            messages.add(receivedMessage);
        }
    }
    public int get(boolean secured, BigInteger exponent, BigInteger modulus) throws IOException {
        byte[] bytes = new byte[1000];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        socket.receive(packet);
        int lastGoodChar=0;
        while (!(bytes[lastGoodChar]==0 && bytes[lastGoodChar+1]==0 && bytes[lastGoodChar+2]==0))
            lastGoodChar++;
        byte[] buf = new byte[lastGoodChar];
        for(int i = 0; i < lastGoodChar;i++){
            buf[i] = bytes[i];
        }
        String tmp;
        if(secured){
            tmp = SecurityProtocol.decryptData(buf,exponent,modulus);
        } else{
            tmp = new String(bytes, 0, packet.getLength());
        }
        System.out.println("\nGot: " + tmp);
        String order = "";
        String receivedMessage = "";
        String receivedChecksum = "";
        int i = 0;
        while (tmp.charAt(i) != ' '){
            order += tmp.charAt(i);
            i++;
        }
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
        if(!newChecksum.equals(receivedChecksum)){
            return -1;
        }
            return Integer.parseInt(receivedMessage);
    }
}
