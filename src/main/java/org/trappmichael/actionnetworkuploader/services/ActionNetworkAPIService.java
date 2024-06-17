package org.trappmichael.actionnetworkuploader.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.trappmichael.actionnetworkuploader.models.ActionNetworkEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ActionNetworkAPIService {

    private final RestClient actionNetworkRestClient;

    private final String ACTIONNETWORK_API_KEY = System.getenv("ACTIONNETWORK_API_KEY");

    public ActionNetworkAPIService() {
        actionNetworkRestClient = RestClient.builder()
                .baseUrl("https://actionnetwork.org/api/v2/")
                .build();
    }
    public void addPerson(String serializedPerson, String apiEndpoint) {
        ResponseEntity<String> result = actionNetworkRestClient.post()
                .uri("/forms/" + apiEndpoint + "/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(serializedPerson)
                .retrieve()
                .toEntity(String.class);

        System.out.println(result.getStatusCode());
    }

    public List<ActionNetworkEntity> getEntities(String formationSelection, String typeSelection) throws JsonProcessingException {

        String entityType;
        int pages;
        List<ActionNetworkEntity> actionNetworkEntities = new ArrayList<>();

        String jsonResponse = actionNetworkRestClient.get()
                .uri(typeSelection)
                .header("Content-Type", "application/json")
                .header("OSDI-API-Token", ACTIONNETWORK_API_KEY)
                .retrieve()
                .body(String.class);

        JsonNode actionNetworkEntityNode = new ObjectMapper().readTree(jsonResponse);

        pages = actionNetworkEntityNode.get("total_pages").asInt();

        List<JsonNode> actionNetworkEntitiesAsJson = StreamSupport
                .stream(actionNetworkEntityNode.get("_embedded").get("osdi:"+ typeSelection).spliterator(),false)
                .toList();

        for (JsonNode actionNetworkEntityAsJson : actionNetworkEntitiesAsJson) {

            ActionNetworkEntity actionNetworkEntity = new ActionNetworkEntity();

            actionNetworkEntity.setType(typeSelection);
            actionNetworkEntity.setTitle(actionNetworkEntityAsJson.get("title").asText());
            actionNetworkEntity.setEndpoint(actionNetworkEntityAsJson.get("identifiers").elements().next().asText().replaceAll("action_network:", ""));

            actionNetworkEntities.add(actionNetworkEntity);
        }

        if (pages > 1) {

            for (int i = 2; i <= pages; i++) {

                jsonResponse = actionNetworkRestClient.get()
                        .uri(typeSelection + "?page=" + i)
                        .header("Content-Type", "application/json")
                        .header("OSDI-API-Token", ACTIONNETWORK_API_KEY)
                        .retrieve()
                        .body(String.class);

                actionNetworkEntitiesAsJson.removeAll(actionNetworkEntitiesAsJson);

                actionNetworkEntitiesAsJson = StreamSupport
                        .stream(actionNetworkEntityNode.get("_embedded").get("osdi:"+ typeSelection).spliterator(),false)
                        .toList();

                for (JsonNode actionNetworkEntityAsJson : actionNetworkEntitiesAsJson) {

                    ActionNetworkEntity actionNetworkEntity = new ActionNetworkEntity();

                    actionNetworkEntity.setType(typeSelection);
                    actionNetworkEntity.setTitle(actionNetworkEntityAsJson.get("title").asText());
                    actionNetworkEntity.setEndpoint(actionNetworkEntityAsJson.get("identifiers").elements().next().asText().replaceAll("action_network:", ""));

                    actionNetworkEntities.add(actionNetworkEntity);
                }
            }
        }

        System.out.println(actionNetworkEntities);

        return actionNetworkEntities;

    }
}
