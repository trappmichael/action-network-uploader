package org.trappmichael.actionnetworkuploader.services;

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

    public static void addPerson(String s) {
        System.out.println(s);
    }

}
