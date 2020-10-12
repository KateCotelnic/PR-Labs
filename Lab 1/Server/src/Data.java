import org.json.JSONException;
import java.io.IOException;

public class Data {
    public String[] all_data = new String[15];
    private int i=0;

    public void addData(String data) throws JSONException, IOException {
        this.all_data[i] = data;
        i++;
        if(i==11) {
            WorkWithData.structure_data(all_data);
        }
    }
}
