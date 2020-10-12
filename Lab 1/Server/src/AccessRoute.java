import org.json.JSONException;
import java.io.IOException;

public class AccessRoute {

    public void doAccessRoute(String link, String header, Data data) throws JSONException, IOException {
        String str = URLConnectionReader.getString(link,header);
        System.out.println(link + ": " + str);
        URLConnectionReader.getLinks(str,header,data);
    }
}
