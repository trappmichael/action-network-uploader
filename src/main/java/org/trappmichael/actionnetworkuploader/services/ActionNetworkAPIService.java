package org.trappmichael.actionnetworkuploader.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.trappmichael.actionnetworkuploader.models.ActionNetworkEntity;

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

    public List<ActionNetworkEntity> getEntities(String formationSelection, String typeSelection) {

        return actionNetworkRestClient.get()
                .uri(typeSelection)
                .header("OSDI-API-Token", ACTIONNETWORK_API_KEY)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ActionNetworkEntity>>() {});
    }
}
