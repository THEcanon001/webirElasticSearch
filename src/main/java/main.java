import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import service.MercadoLibreService;
import service.OlxService;

import java.io.IOException;

public class main {
    public final static void main(String[] args) throws IOException, JSONException, UnirestException {
        init();
    }


    private static void init() throws IOException, UnirestException {
        //todo if(empty,ElasticSearch)
        MercadoLibreService.loadVehiclesML();
        OlxService.loadVehicles();
    }
}
