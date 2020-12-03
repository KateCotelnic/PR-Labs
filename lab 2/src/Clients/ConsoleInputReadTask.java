package Clients;
import java.io.*;
import java.util.concurrent.*;

class ConsoleInputReadTask implements Callable<String> {
    public String call() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String input;
        while (true) {
            try {
                while (!br.ready()) {
                    Thread.sleep(200);
                }
                input = br.readLine();
            } catch (InterruptedException e) {
                return null;
            }
            if (!("".equals(input))) break;
        }
        return input;
    }
}