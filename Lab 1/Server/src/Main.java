import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws JSONException, IOException {

        String access = "/register";
        String first_header = "";
        String str = URLConnectionReader.getString(access, first_header);
        System.out.printf(access + ": " + str);
        JSONObject obj = new JSONObject(str);

        access = "/home";
        first_header = "access_token";
        String header = obj.getString(first_header);
        str = URLConnectionReader.getString(access,header);
        System.out.printf(access + ": " + str);

        Data all_data = new Data();
        String finalStr = str;
        URLConnectionReader.getLinks(finalStr, header, all_data);
    }
}
