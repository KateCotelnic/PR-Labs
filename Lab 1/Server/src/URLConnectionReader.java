import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class URLConnectionReader {

    public static String getString(String url, String header) {
        HttpURLConnection c = null;
        try {
            String mainlink = "http://localhost:5000";
            URL u = new URL(mainlink + url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("X-Access-Token",header);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(URLConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(URLConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(URLConnectionReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public static void getLinks(String access, String header, Data data) throws JSONException, IOException {

        JSONObject obj = new JSONObject(access);

        if(obj.has("data")) {
            String datastr = obj.getString("data");
            data.addData(datastr);
        }

        if(obj.has("link")) {
            JSONObject object = new JSONObject(obj.getString("link"));

            ExecutorService executorService = Executors.newFixedThreadPool(6);
            for (Iterator it = object.keys(); it.hasNext(); ) {
                Object key = it.next();
                String keyStr = (String) key;
                Object keyvalue = object.get(keyStr);

                AccessRoute accessRoute = new AccessRoute();
                AccessRouteThread thread1 = new AccessRouteThread(accessRoute, keyvalue + "", header,data);
                executorService.execute(thread1);
            }
            executorService.shutdown();
            try{
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
            }
        }
    }
}