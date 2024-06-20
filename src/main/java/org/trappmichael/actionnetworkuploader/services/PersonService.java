package org.trappmichael.actionnetworkuploader.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // Imports a .csv file of person records to an Action Network API endpoint by reading the .csv file
    // one line at a time, parsing each line into a Person java object, serializing each Person java object
    // into a json string compatible with the Action Network API, and uploading each person to the API.
    public void importCSV(String actionNetworkEntityType, String actionNetworkEntityApiEndpoint, InputStream csvFileStream) {

        InputStreamReader inputStreamReader = new InputStreamReader(csvFileStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        bufferedReader.lines()
                    .skip(1)
                    .limit(500)
                    .map(Person::parse)
                    .map(person -> {
                        try {
                            return new ObjectMapper().writeValueAsString(person);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(serializedPerson -> {
                        actionNetworkAPIService.addPerson(serializedPerson, actionNetworkEntityType, actionNetworkEntityApiEndpoint);
                    });
    }
}
