import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import service.ElasticSearchService;
import service.MercadoLibreService;
import service.OlxService;

import java.io.IOException;

public class main {


    public static void main(String[] args) throws IOException, JSONException, UnirestException {
        ElasticSearchService.create();
        //init();
    }


    private static void init() throws IOException, UnirestException {
        MercadoLibreService.loadVehiclesML();
        OlxService.loadVehicles();
    }
}
