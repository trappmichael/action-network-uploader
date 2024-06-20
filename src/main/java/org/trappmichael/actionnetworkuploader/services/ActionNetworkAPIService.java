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

    // Add a person record to an Action Network API endpoint
    public void addPerson(String serializedPerson, String actionNetworkEntityType, String actionNetworkEntityApiEndpoint) {

        String importType = null;

        if (actionNetworkEntityType.equals("forms")) {
            importType = "submissions";
        } else if (actionNetworkEntityType.equals("events")) {
            importType = "attendances";
        } else if (actionNetworkEntityType.equals("petitions")) {
            importType = "signatures";
        }

        ResponseEntity<String> result = actionNetworkRestClient.post()
                .uri(actionNetworkEntityType + "/" + actionNetworkEntityApiEndpoint + "/" + importType)
                .contentType(MediaType.APPLICATION_JSON)
                .body(serializedPerson)
                .retrieve()
                .toEntity(String.class);

    }

    // Retrieve an array list of Action Network entities of the selected type from the selected formation
    public List<ActionNetworkEntity> getEntities(String formationSelection, String typeSelection) throws JsonProcessingException {

        List<ActionNetworkEntity> actionNetworkEntities = new ArrayList<>();

        String jsonResponse = actionNetworkRestClient.get()
                .uri(typeSelection)
                .header("Content-Type", "application/json")
                .header("OSDI-API-Token", System.getenv(formationSelection))
                .retrieve()
                .body(String.class);

        // Maps the nodes of the json response containing the Action Network entities
        JsonNode jsonResponseNode = new ObjectMapper().readTree(jsonResponse);

        // Travels to the node containing an array list of json objects representing individual
        // Action Network entities, serializes each of them as an ActionNetworkEntity java object,
        // and adds the new ActionNetworkEntity java object to an array list.
        StreamSupport.stream(jsonResponseNode.get("_embedded").get("osdi:"+ typeSelection).spliterator(),false)
                .forEach(actionNetworkEntity -> {
                    try {
                        ActionNetworkEntity deserializedActionNetworkEntity = new ObjectMapper().readValue(actionNetworkEntity.toString(), ActionNetworkEntity.class);
                        actionNetworkEntities.add(deserializedActionNetworkEntity);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        // The Action Network API returns a maximum of 25 entities per response. Retrieves the
        // total number of pages of entities.
        int pages = jsonResponseNode.get("total_pages").asInt();

        // If the total number of pages of response entities is greater than 1, repeats the process above
        // of retrieving entities from the Action Network API for each page of available entities.
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

        return actionNetworkEntities;

    }
}
