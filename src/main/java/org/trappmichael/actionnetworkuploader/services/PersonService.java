package org.trappmichael.actionnetworkuploader.services;

import org.springframework.stereotype.Service;
import org.trappmichael.actionnetworkuploader.models.Person;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class PersonService {

    private final ActionNetworkAPIService actionNetworkAPIService;

    public PersonService(ActionNetworkAPIService actionNetworkAPIService) {
        this.actionNetworkAPIService = actionNetworkAPIService;
    }

    public void importCSV(InputStream csvFileStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(csvFileStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        bufferedReader.lines()
                .skip(1)
                .limit(500)
                .map(Person::parse)
                .forEach(actionNetworkAPIService::addPerson);
    }
}
