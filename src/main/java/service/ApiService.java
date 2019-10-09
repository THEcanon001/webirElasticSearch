package service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@LocalBean
@Path("")
public class ApiService {

    @GET
    @Path("welcome")
    @Produces({MediaType.APPLICATION_JSON})
    public String status() {
        return "Escuchando peticiones...";
    }

    @POST
    @Path("load")
    @Produces({MediaType.APPLICATION_JSON})
    public String load() {
        return "ok";
    }
}
