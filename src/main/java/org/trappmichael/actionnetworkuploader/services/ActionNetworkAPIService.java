package org.trappmichael.actionnetworkuploader.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.trappmichael.actionnetworkuploader.models.Person;

@Service
public class ActionNetworkAPIService {

    private final RestClient actionNetworkRestClient;

    public ActionNetworkAPIService() {
        actionNetworkRestClient = RestClient.builder()
                .baseUrl("https://actionnetwork.org/api/v2/")
                .build();
    }

    public Person addPerson(Person person) {
        return null;
    }
}
