package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Vehicle;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public final class ElasticSearchService {

    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");

    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;

    private static final String INDEX = "vehicledata";
    private static final String TYPE = "vehicle";

    private static final Integer SIZE = 10000;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private ElasticSearchService() {
    }

    public static void insert(ArrayList<Vehicle> vehicles) throws IOException {
        for (Vehicle v : vehicles) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            String id = UUID.randomUUID().toString();
            v.setId(id);
            dataMap.put("id", id);
            dataMap.put("title", v.getTitle());
            dataMap.put("brand", v.getBrand());
            dataMap.put("condition", v.getCondition());
            dataMap.put("currency", v.getCurrency());
            dataMap.put("price", Integer.toString(v.getPrice()));
            dataMap.put("photos", v.getPhotos());
            IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, id)
                    .source(dataMap);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }

    //TODO
    public static List<Vehicle> getAllVehicles() {
        return null;
    }

    public static List<Vehicle> getVehicles(String filter_type, String filter) {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchQuery(filter_type, filter));
            searchSourceBuilder.size(SIZE);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit searchHit : searchHits) {
                Vehicle vehicle = searchHit != null ?
                        objectMapper.convertValue(searchHit.getSourceAsMap(), Vehicle.class) : null;
                if(vehicle != null)
                    vehicle.setBrand(filter);
                vehicles.add(vehicle);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return vehicles;
    }

    //TODO
    public static List<Vehicle> getVehicles(List<String> filter_type_list, List<String> filter_list) {
        return null;
    }

    public static boolean exist() throws IOException {
        GetIndexRequest request = new GetIndexRequest().indices(INDEX);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    public static void createClient() {
        if (restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }
    }

    public static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public static void info() throws IOException {
        MainResponse response = restHighLevelClient.info(RequestOptions.DEFAULT);
        String clusterName = response.getClusterName();
        String clusterUuid = response.getClusterUuid();
        String nodeName = response.getNodeName();
        MainResponse.Version version = response.getVersion();
        LOGGER.info("Información del cluster: ");
        LOGGER.info("Nombre del cluster: {} " + clusterName);
        LOGGER.info("Identificador del cluster: {} " + clusterUuid);
        LOGGER.info("Nombre de los nodos del cluster: {} " + nodeName);
        LOGGER.info("Versión de elasticsearch del cluster: {} " + version.getBuildDate()+" "+version.getBuildFlavor()+" "+ version.getBuildType());
    }
}
