import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static FinalData data = WorkWithData.getFinalData();

    public static void go(){
        try (ServerSocket serverSocket = new ServerSocket(3000)){
            while(true){
                new EchoServer(serverSocket.accept()).start();
            }
        } catch (IOException e){
        }
    }
}
