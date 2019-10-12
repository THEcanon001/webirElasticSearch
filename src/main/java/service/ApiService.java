package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Vehicle;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
            List<Vehicle> vehicleList = ElasticSearchService.getVehicles(filter_Type, filter);
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
        return null;
    }

    @GET
    @Path("vehicles_filters")
    @Produces({MediaType.APPLICATION_JSON})
    public Response load_many_filters(@QueryParam("filter_type_list") String filter_type, @QueryParam("filter_list") String filter) throws IOException {
        try {
            List<String> filter_type_list = new ArrayList<>();
            List<String> filter_list = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            byte[] filter_type_byte_array = DatatypeConverter.parseBase64Binary(filter_type);
            String filter_type_type_json = new String(filter_type_byte_array);
            byte[] filter_byte_array = DatatypeConverter.parseBase64Binary(filter_type);
            String filter_type_json = new String(filter_byte_array);
            try {
                filter_type_list = mapper.readValue(filter_type_type_json, List.class);
                filter_list = mapper.readValue(filter_type_json, List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ElasticSearchService.createClient();
            List<Vehicle> vehicleList = ElasticSearchService.getVehicles(filter_type_list, filter_list);
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
