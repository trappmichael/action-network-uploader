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
import java.util.List;

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

        String jsonResponse = actionNetworkRestClient.get()
                .uri(typeSelection)
                .header("Content-Type", "application/json")
                .header("OSDI-API-Token", ACTIONNETWORK_API_KEY)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();

        String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonResponse);

        System.out.println(prettyJson);

//        JsonNode actionNetworkEntityNode = new ObjectMapper().readTree(jsonResponse);

        ActionNetworkEntity sample = new ActionNetworkEntity("form", "event", "38f8f");

        List<ActionNetworkEntity> sampleList = new ArrayList<>();

        sampleList.add(sample);

        return sampleList;

    }
}
