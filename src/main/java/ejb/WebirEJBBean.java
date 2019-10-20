package ejb;

import service.ElasticSearchService;
import service.MercadoLibreService;
import service.GallitoService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.logging.Logger;

@LocalBean
@Stateless
public class WebirEJBBean {
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");

    public void init() {
        try {
            LOGGER.info("Carga inicial en proceso...");
            ElasticSearchService.createClient();
            ElasticSearchService.info();
            if (!ElasticSearchService.exist()) {
                LOGGER.info("Cargando vehiculos de mercadolibre");
                MercadoLibreService.loadVehiclesML();
                GallitoService.loadVehicles();
                LOGGER.info("Cargando vehiculos del gallito");
            }
            ElasticSearchService.closeConnection();
            LOGGER.info("Carga inicial finalizada con exito");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update() {
    }
}
