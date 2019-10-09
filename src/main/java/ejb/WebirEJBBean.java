package ejb;

import com.mashape.unirest.http.exceptions.UnirestException;
import service.ElasticSearchService;
import service.MercadoLibreService;
import service.OlxService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.io.IOException;

@LocalBean
@Stateless
public class WebirEJBBean {
    private boolean init;
    public void init() throws IOException, UnirestException {
        if(!init) {
            System.out.println("No cargue");
            init = true;
        } else
            System.out.println("Cargue");
//        ElasticSearchService.createClient();
//        MercadoLibreService.loadVehiclesML();
//        OlxService.loadVehicles();
    }
}
