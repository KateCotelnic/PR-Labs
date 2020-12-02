package Protocols.Transport;

import Protocols.Session.SecurityProtocol;

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
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class TransportProtocol {
    DatagramSocket socket;
    InetAddress address;
    int port;
    private long maxSize = 20;
    private String data;
    private List<String> messages;
    private boolean secured = false;

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    public TransportProtocol(DatagramSocket socket, InetAddress address, int port) throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        this.socket = socket;
        this.address = address;
        this.port = port;
//        if(is_sender){
//            prepare_for_send();
//        }
//        else{
//            prepare_for_receive();
//        }
    }

    public void send(String data) throws NoSuchAlgorithmException, InterruptedException {
        this.data = data;
        messages = new ArrayList<>();
        sendMessages();
    }


    public String receive() throws IOException, InterruptedException {
        data = "";
        messages = new ArrayList<>();
        String s = receiveMessages();
        return  s;
    }

//    private void prepare_for_send() throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
////        securityProtocol = securityProtocol();
//        String key = receive();
//        System.out.println("key = " + key);
//    }

//    public void prepare_for_receive() throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InterruptedException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
////        securityProtocol = new SecurityProtocol();
////        String public_modulus = "" + securityProtocol.getPublic_modulus();
////        String public_exponent = "" + securityProtocol.getPublic_exponent();
////        String public_key = public_exponent + " " + public_modulus;
//        System.out.println("pm = " + public_modulus);
//        System.out.println("pe = " + public_exponent);
//        messages = new ArrayList<>();
//        send(public_key);
//    }

    private void sendMessages() throws NoSuchAlgorithmException, InterruptedException {
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
        boolean finished = executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    private void generateMessages(){
        int nrDigits = 0, nr = messages.size();
        while (nr != 0) {
            nr /= 10;
            ++nrDigits;
        }
//        maxSize = maxSize - 3 - 2 - 3;
        String tmp;
        while (data.length()>=maxSize) {
            System.out.println("max = " + maxSize);
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
//        maxSize = 10;
        messages = new ArrayList<>();
        String data = "";
        Receiver receiver = new Receiver(socket);
//        receiver.n = 0;

//        Thread thread = new Thread(receiver);
//        thread.start();
//        Thread.sleep(150);
//        receiver.kill();
//        thread.interrupt();
//        System.out.println("transport 140");
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
