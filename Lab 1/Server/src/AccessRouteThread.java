import org.json.JSONException;
import java.io.IOException;

public class AccessRouteThread implements Runnable {
        private AccessRoute threadRoute;
        private String link;
        private String header;
        private Data data;

        public AccessRouteThread(AccessRoute route, String link, String header, Data data){
            threadRoute = route;
            this.link = link;
            this.header = header;
            this.data = data;
        }

        public void run() {
            try {
                threadRoute.doAccessRoute(this.link, this.header, this.data);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
}
