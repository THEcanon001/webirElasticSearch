package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Filter;
import entity.Vehicle;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.NameTokenizers;

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

    @GET
    @Path("vehicles")
    @Produces({MediaType.APPLICATION_JSON})
    public Response load(@QueryParam("filter_type") String filter_Type, @QueryParam("filter") String filter) throws IOException {
        try{
            ElasticSearchService.createClient();
            List<Vehicle> vehicleList = ElasticSearchService.getVehicles(new Filter(filter_Type, filter));
            ElasticSearchService.closeConnection();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.CAMEL_CASE)
                    .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);

            Type targetListType = new TypeToken<List<Vehicle>>() {
            }.getType();

            List list = modelMapper.map(vehicleList, targetListType);
            return Response.status(Response.Status.OK).entity(list).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
        }
    }

    @GET
    @Path("vehicles_all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response load_all() throws IOException {
        ElasticSearchService.createClient();
        List<Vehicle> vehicleList = ElasticSearchService.getAllVehicles();
        ElasticSearchService.closeConnection();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.CAMEL_CASE)
                .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);

        Type targetListType = new TypeToken<List<Vehicle>>() {
        }.getType();

        List list = modelMapper.map(vehicleList, targetListType);
        return Response.status(Response.Status.OK).entity(list).build();
    }

    @POST
    @Path("sendToken")
    //@Consumes("application/json")
    public void sendToken(String token) throws IOException {
        int a = 1;
        a = a + 1;
    }

    @GET
    @Path("vehicles_filters")
    @Produces({MediaType.APPLICATION_JSON})
    public Response load_many_filters(@QueryParam("filters") String filters) throws IOException {
        try {
            JSONObject json = new JSONObject(filters);
            JSONArray filtersArray = json.getJSONArray("filters");
            ArrayList<Filter> filterList = new ArrayList<>();
            for (int j = 0; j < filtersArray.length(); j++)
            {
                JSONObject filter = filtersArray.getJSONObject(j);
                filterList.add(new Filter(filter.getString("type"), filter.getString("value")));
            }

            ElasticSearchService.createClient();
            List<Vehicle> vehicleList = ElasticSearchService.getVehicles(filterList);
            ElasticSearchService.closeConnection();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.CAMEL_CASE)
                    .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);

            Type targetListType = new TypeToken<List<Vehicle>>() {
            }.getType();

            List list = modelMapper.map(vehicleList, targetListType);
            return Response.status(Response.Status.OK).entity(list).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
        }
    }
}
