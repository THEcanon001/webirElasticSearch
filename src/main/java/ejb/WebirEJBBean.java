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

    public void init() throws IOException, UnirestException {
        System.out.println("CARGA INICIAL DE DATOS ;)");
        try {
            //ElasticSearchService.info();
            //ElasticSearchService.createClient();
            //MercadoLibreService.loadVehiclesML();
            //OlxService.loadVehicles();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
