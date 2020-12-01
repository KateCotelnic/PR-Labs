import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost",3000)){
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter printString = new PrintWriter(socket.getOutputStream(),true);

            Scanner scanner = new Scanner(System.in);
            String string;
            String response;
            do{
                System.out.println("\nEnter the one of the following commands: \n           SelectAllColumnNames\n           SelectColumn <column_name> \n           SelectFromColumn <column_name> <search_for>\n           SelectRow <row_index>\n           SelectRowWith <search_for>\n           Exit");
                string = scanner.nextLine();

                printString.println(string);
                if(!string.equals("Exit")){
                    response = reader.readLine();
                    System.out.println(response);
                }
            } while (!string.equals("Exit"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
