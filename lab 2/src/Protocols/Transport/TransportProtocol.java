package Protocols.Transport;

//import Protocols.Session.SecurityProtocol;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class TransportProtocol {
    DatagramSocket socket;
    InetAddress address;
    int port;
    private long maxSize;
    private String data;
    private List<String> messages;
//    private SecurityProtocol securityProtocol;
//    private AESEncryption aesEncryption;

    public void send(DatagramSocket socket, InetAddress address, int port, String data) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {

        this.data = data;
        this.socket = socket;
        this.address = address;
        this.port = port;
        maxSize = 10;
        messages = new ArrayList<>();
        prepate_for_send();
//        byte[] bytes = new byte[10000];
//        DatagramPacket packet1 = new DatagramPacket(bytes,bytes.length);
//        socket.receive(packet1);
//        String key = new String(bytes);
//        System.out.println("key = " + key);
//        String[] arrOfStr = key.split(" ", 5);
//        BigInteger modulus = new BigInteger(arrOfStr[0]);
//        System.out.println("\nmod: " + modulus);
//        int i = 0;
//        while (arrOfStr[1].charAt(i) >= '0' && arrOfStr[1].charAt(i) <= '9')
//            i++;
//        String s = arrOfStr[1].substring(0,i);
//        BigInteger exponent = new BigInteger(s);
//        System.out.println("\nexp: " + exponent);
//        securityProtocol = new SecurityProtocol(modulus,exponent);
//        this.data = new String(securityProtocol.getKeys().encryptData(data,modulus,exponent));
        sendMessages();
    }


    public String receive(DatagramSocket socket, InetAddress address) throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
        this.socket = socket;
        data = "";
        maxSize = 10;
        messages = new ArrayList<>();
//        securityProtocol = new SecurityProtocol();
//        System.out.println("public modulus: " + securityProtocol.getKeys().getPublic_modulus());
//        System.out.println("public exp: " + securityProtocol.getKeys().getPublic_exponent());
//        String key_to_send = securityProtocol.getKeys().getPublic_modulus() + " " + securityProtocol.getKeys().getPublic_exponent();
//        this.aesEncryption = new AESEncryption();
//        Key key = this.aesEncryption.generateKey();
//        byte[] buffer = key_to_send.getBytes();
//        System.out.println(key_to_send);
//        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3000);
//        socket.send(packet);
        prepare_for_receive();
        String s = receiveMessages();
        return  s;
    }

    private void prepate_for_send(){

    }

    public void prepare_for_receive(){

    }

    private void sendMessages() throws NoSuchAlgorithmException {
        generateMessages();
        byte[] bytes = messages.get(messages.size()-1).getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nsend: " + messages.get(messages.size()-1));
        messages.remove(messages.size()-1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

//        String key = "l5ukpxmtg7u8ko/t";
        for(String message : messages){
//            SecurityProtocol securityProtocol = new SecurityProtocol(true,message);
//            message = securityProtocol.getMessage();
            Sender sender = new Sender(socket,address,port,message);
            executorService.execute(sender);
        }
        executorService.shutdown();
    }

    private void generateMessages(){
        int nrDigits = 0, nr = messages.size();
        while (nr != 0) {
            nr /= 10;
            ++nrDigits;
        }
        maxSize = maxSize - 3 - 2 - nrDigits;
        String tmp;
        while (data.length()>=maxSize) {
            tmp = data.substring(0, (int) maxSize);
            data = data.substring((int) maxSize);
            byte[] bytes = tmp.getBytes();
            tmp = messages.size() + " " + tmp + "/" + getCRC32Checksum(bytes) + "*";
            messages.add(tmp);
//            System.out.println(tmp);
        }
        tmp = data;
        byte[] bytes = tmp.getBytes();
        tmp = messages.size() + " " + tmp + "/" + getCRC32Checksum(bytes) + "*";
        messages.add(tmp);
        tmp = messages.size() + "";
        bytes = tmp.getBytes();
        tmp = messages.size() + " " + tmp + "/" + getCRC32Checksum(bytes) + "*";
        messages.add(tmp);
    }

    private String receiveMessages() throws IOException, InterruptedException {
        maxSize = 10;
        messages = new ArrayList<>();
        String data = "";
        Receiver receiver = new Receiver(socket);
//        receiver.n = 0;

//        Thread thread = new Thread(receiver);
//        thread.start();
//        Thread.sleep(150);
//        receiver.kill();
//        thread.interrupt();
        int n = receiver.get();
//        System.out.println("n = " + n);
//        int i = 0;
        while (n>0){
            if (!Receiver.is_ok){
                break;
            }
            receiver.doOnce();
            n--;
        }
        if (Receiver.is_ok && n >= 0) {
            data = "";
//            System.out.println("\neverything is ok");
//            System.out.println(receiver.getOrders().toString());
            String[] mes = new String[receiver.getMessages().size()];
//            System.out.println();
//            System.out.println(receiver.getMessages().size());
//            System.out.println();
            for(int i = 0; i < receiver.getMessages().size(); i++){
//                System.out.println(receiver.getOrders().get(i) + ": " + receiver.getMessages().get(i));
                mes[receiver.getOrders().get(i)] = receiver.getMessages().get(i);
            }
//            System.out.println(mes.length);
//            System.out.println();
            for(int i = 0; i < mes.length; i++){
//                System.out.println(i + ": \"" + mes[i] + "\"");
                data += mes[i];
            }
//            System.out.println("\ndata = " + data);
        }
        else {
            if(Receiver.attempts > 0) {
                Receiver.attempts--;
                System.out.println("failed");
                String string = "again";
                Receiver.is_ok = true;
                InetAddress address = InetAddress.getLocalHost();
                byte[] buffer = string.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3000);
                socket.send(packet);
                receiveMessages();
            }
            else
                System.out.println("sorry, too much attempts");
        }
//        System.out.println("data = " + data);
//        System.out.println("done");
        return data;
    }

    public static long getCRC32Checksum(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

}
