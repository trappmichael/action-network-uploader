package org.trappmichael.actionnetworkuploader.services;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ActionNetworkAPIService {

    private final RestClient actionNetworkRestClient;

    public ActionNetworkAPIService() {
        actionNetworkRestClient = RestClient.builder()
                .baseUrl("https://actionnetwork.org/api/v2/")
                .build();
    }

    public ResponseEntity<Void> addPerson(String serializedPerson, String apiEndpoint) {
        return actionNetworkRestClient.post()
                .uri("/forms/" + apiEndpoint + "/submissions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(serializedPerson)
                .retrieve()
                .toBodilessEntity();
    }
}
