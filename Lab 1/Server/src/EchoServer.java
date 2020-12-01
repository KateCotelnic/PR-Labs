import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoServer extends Thread{
    private Socket socket;

    public EchoServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            while (true){
                String readString = input.readLine();
                OperationExecutor operationExecutor = new OperationExecutor();
                if(readString.startsWith("SelectAllColumnNames")) {
                    String str = operationExecutor.executeOperation(new SelectAllColumnNamesOperation(new CommandExecutors(readString)));
                    output.println(str);
                }

                if(readString.startsWith("SelectColumn")){
                    String str = operationExecutor.executeOperation(new SelectColumnOperation(new CommandExecutors(readString)));
                    output.println(str);
                }

                else if (readString.startsWith("SelectFromColumn")){
                    String str = operationExecutor.executeOperation(new SelectFromColumnOperation(new CommandExecutors(readString)));
                    output.println(str);
                }

                else if(readString.startsWith("SelectRowWith")){
                    String str = operationExecutor.executeOperation(new SelectRowWithOperation(new CommandExecutors(readString)));
                    output.println(str);
                }

                else if (readString.startsWith("SelectRow ")){
                    String str = operationExecutor.executeOperation(new SelectRowOperation(new CommandExecutors(readString)));
                    output.println(str);
                }

                else if (readString.equals("Exit")){
                    break;
                }
                else if (readString.equals("")){
                    output.println("Enter the command");
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e){
            }
        }
    }
}
