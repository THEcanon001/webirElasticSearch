package service;


import com.google.gson.Gson;
import entity.Vehicle;
import org.apache.http.HttpHost;
import org.elasticsearch.Version;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
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
import java.util.logging.Logger;

public final class ElasticSearchService {

    private final static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");

    private ElasticSearchService(){
    }

    public static void insert(ArrayList<Vehicle> vehicles) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        LOGGER.info("Cliente conectado. ");

        BulkRequest request = new BulkRequest();
        int vehicleCount = 1;
        for(Vehicle vehicle :vehicles) {
            Gson gson = new Gson();
            String JSON = gson.toJson(vehicle);
            request.add(new IndexRequest("vehicles", "vehicle", String.valueOf(vehicleCount)).source(XContentType.JSON, JSON));
            vehicleCount++;
        }

        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        LOGGER.info("Bulk con errores: {} "+ bulkResponse.hasFailures());

        client.close();
        LOGGER.info("Cliente desconectado.");
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


    public static void create() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        LOGGER.info("Cliente conectado. ");

        CreateIndexRequest request = new CreateIndexRequest("vehicles");
        String JSON = "{\n" +
                "  \"properties\": {\n" +
                "    \"title\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"price\": {\n" +
                "      \"type\": \"number\"\n" +
                "    },\n" +
                "    \"currency\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"condition\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"photos\": {\n" +
                "      \"type\": \"text\"\n" +
                "    },\n" +
                "    \"brand\": {\n" +
                "      \"type\": \"text\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        request.mapping("vehicle", JSON, XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

        boolean acknowledged = createIndexResponse.isAcknowledged();
        LOGGER.info("Indice creado: {} " + acknowledged);

        client.close();
        LOGGER.info(" Cliente desconectado.");
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
