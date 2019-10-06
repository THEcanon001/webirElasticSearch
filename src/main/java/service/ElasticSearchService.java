package service;


import com.google.gson.Gson;
import entity.Vehicle;
import org.apache.http.HttpHost;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.Version;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class ElasticSearchService {

    private final static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");

    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String INDEX = "persondata";
    private static final String TYPE = "person";

    private ElasticSearchService(){
    }

    public static void insert(ArrayList<Vehicle> vehicles) throws IOException {
        for (Vehicle v : vehicles){
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
             restHighLevelClient.index(indexRequest);
            //GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
            //GetResponse getResponse = null;

            //getResponse = restHighLevelClient.get(getRequest);
        }
    }

    public static void update(){
        //TODO Actualizacion futura ;)
    }

    public static void select() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        LOGGER.info("Cliente conectado.");

        SearchRequest searchRequest = new SearchRequest("vehicles");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit hit: searchResponse.getHits().getHits()){
            LOGGER.info("Documento con id {}: {} "+ hit.getId() +" - " + hit.getSourceAsString());
        }

        client.close();
        LOGGER.info("Cliente desconectado.");
    }


    public static void createClient() throws IOException {

        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }
    }

    public static void test() throws IOException {
        MainResponse response = client.info(RequestOptions.DEFAULT);
        ClusterName clusterName = response.getClusterName();
        String clusterUuid = response.getClusterUuid();
        String nodeName = response.getNodeName();
        Version version = response.getVersion();
        LOGGER.info("Información del cluster: ");
        LOGGER.info("Nombre del cluster: {} " + clusterName.value());
        LOGGER.info("Identificador del cluster: {} "+  clusterUuid);
        LOGGER.info("Nombre de los nodos del cluster: {} " + nodeName);
        LOGGER.info("Versión de elasticsearch del cluster: {} "+ version.toString());
        client.close();
        LOGGER.info("Cliente desconectado.");
    }
}
