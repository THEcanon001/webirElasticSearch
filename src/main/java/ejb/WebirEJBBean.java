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
            ElasticSearchService.createClient();
            ElasticSearchService.info();
            if (!ElasticSearchService.exist()) {
                LOGGER.info("Carga inicial en proceso...");
                LOGGER.info("Cargando vehiculos de mercadolibre");
                MercadoLibreService.loadVehiclesML();
                GallitoService.loadVehicles();
                LOGGER.info("Cargando vehiculos del gallito");
            } else{
                LOGGER.info("Actualizacion de vehículos en proceso...");
                LOGGER.info("Actualizando vehiculos de mercadolibre");
                //todo update
                LOGGER.info("Actualizando vehiculos del gallito");
            }
            ElasticSearchService.closeConnection();
            LOGGER.info("Carga/actualizacion finalizada con exito");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
