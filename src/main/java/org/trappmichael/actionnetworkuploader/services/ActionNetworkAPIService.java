package org.trappmichael.actionnetworkuploader.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.trappmichael.actionnetworkuploader.models.ActionNetworkEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ActionNetworkAPIService {

    private final RestClient actionNetworkRestClient;

    public ActionNetworkAPIService() {
        actionNetworkRestClient = RestClient.builder()
                .baseUrl("https://actionnetwork.org/api/v2/")
                .build();
    }
    public void addPerson(String serializedPerson, String actionNetworkEntityType, String actionNetworkEntityApiEndpoint) {

        String importType = null;

        if (actionNetworkEntityType.equals("forms")) {
            importType = "submissions";
        } else if (actionNetworkEntityType.equals("events")) {
            importType = "attendances";
        } else if (actionNetworkEntityType.equals("petitions")) {
            importType = "signatures";
        }

        System.out.println(actionNetworkEntityType);
        System.out.println(actionNetworkEntityApiEndpoint);
        System.out.println(importType);

        ResponseEntity<String> result = actionNetworkRestClient.post()
                .uri(actionNetworkEntityType + "/" + actionNetworkEntityApiEndpoint + "/" + importType)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serializedPerson)
                .retrieve()
                .toEntity(String.class);

        System.out.println(result.getStatusCode());
    }

    public List<ActionNetworkEntity> getEntities(String formationSelection, String typeSelection) throws JsonProcessingException {

        List<ActionNetworkEntity> actionNetworkEntities = new ArrayList<>();

        String jsonResponse = actionNetworkRestClient.get()
                .uri(typeSelection)
                .header("Content-Type", "application/json")
                .header("OSDI-API-Token", System.getenv(formationSelection))
                .retrieve()
                .body(String.class);

        JsonNode jsonResponseNode = new ObjectMapper().readTree(jsonResponse);

        StreamSupport.stream(jsonResponseNode.get("_embedded").get("osdi:"+ typeSelection).spliterator(),false)
                .forEach(actionNetworkEntity -> {
                    try {
                        ActionNetworkEntity deserializedActionNetworkEntity = new ObjectMapper().readValue(actionNetworkEntity.toString(), ActionNetworkEntity.class);
                        actionNetworkEntities.add(deserializedActionNetworkEntity);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        int pages = jsonResponseNode.get("total_pages").asInt();

        if (pages > 1) {

            for (int i = 2; i <= pages; i++) {

                String page = String.valueOf(i);

                jsonResponse = actionNetworkRestClient.get()
                        .uri(typeSelection + "?page=" + page)
                        .header("Content-Type", "application/json")
                        .header("OSDI-API-Token", System.getenv(formationSelection))
                        .retrieve()
                        .body(String.class);

                jsonResponseNode = new ObjectMapper().readTree(jsonResponse);

                StreamSupport.stream(jsonResponseNode.get("_embedded").get("osdi:"+ typeSelection).spliterator(),false)
                        .forEach(actionNetworkEntity -> {
                            try {
                                String json = actionNetworkEntity.asText();
                                ActionNetworkEntity deserializedActionNetworkEntity = new ObjectMapper().readValue(actionNetworkEntity.toString(), ActionNetworkEntity.class);
                                actionNetworkEntities.add(deserializedActionNetworkEntity);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }

        System.out.println(actionNetworkEntities);

        return actionNetworkEntities;

    }
}
