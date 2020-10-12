import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void go(FinalData data){
        try (ServerSocket serverSocket = new ServerSocket(3000)){
            Socket socket = serverSocket.accept();
            System.out.println("Client Connected");
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            while (true){
                String readString = input.readLine();

                if(readString.equals("SelectAllColumnNames")){
                    String header = "";
                    for (int k =0;k<data.getHeader().length;k++){
                        if(data.getHeader()[k]!=null) {
                            header += data.getHeader()[k] + "   ";
                        }
                    }
                    output.println(header);
                }

                else if(readString.startsWith("SelectColumn")){
                    int i=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    String s = readString.substring(i+1);
                    int j=0;
                    while (!data.getHeader()[j].equals(s)) {
                        j++;
                    }
                    s = "";
                    for(i =0; i<data.getTable().length;i++){
                        if(data.getTable()[i][j]!=null){
                            s+=data.getTable()[i][j] + "  ";
                        }
                    }
                    output.println(s);
                }

                else if (readString.startsWith("SelectFromColumn")){
                    int i=0; int k=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    k = i+1;
                    i++;
                    while (readString.charAt(i) != ' ') {
                        i++;
                    }
                    String s = readString.substring(k,i);
                    String name = readString.substring(i+1);
                    int j=0;
                    while (!data.getHeader()[j].equals(s)) {
                        j++;
                    }
                    s = "";
                    for(i =0; i<data.getTable().length;i++){
                        if(data.getTable()[i][j]!=null && data.getTable()[i][j].contains(name)){
                            s+=data.getTable()[i][j] + "  ";
                        }
                    }
                    output.println(s);
                }

                else if(readString.startsWith("SelectRowWith")){
                    int i=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    String name = readString.substring(i+1);
                    System.out.println(name);
                    String s = "";
                    for(i =0; i<data.getTable().length;i++){
                        for(int j=0; j<data.getTable()[i].length;j++){
                            if(data.getTable()[i][j]!=null && data.getTable()[i][j].contains(name)) {
                                for(int k=0;k<data.getTable()[i].length;k++){
                                    if(data.getTable()[i][j]!=null){
                                        s += data.getTable()[i][k] + "  ";
                                    }
                                }
                                output.println(s);
                                break;
                            }
                        }
                    }
                }

                else if (readString.startsWith("SelectRow ")){
                    int i=0;
                    while (readString.charAt(i)!=' '){
                        i++;
                    }
                    String s = readString.substring(i+1);
                    i = Integer.parseInt(s);
                    s = "";
                    for(int j = 0; j < data.getTable()[0].length; j++){
                        if(data.getTable()[i][j] != null){
                            s += data.getTable()[i][j] + "  ";
                        }
                    }
                    output.println(s);
                }

                else if (readString.equals("Exit")){
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
