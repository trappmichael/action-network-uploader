package org.trappmichael.actionnetworkuploader.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.trappmichael.actionnetworkuploader.models.Person;
import org.trappmichael.actionnetworkuploader.models.PersonSerializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class PersonService {

    private final ActionNetworkAPIService actionNetworkAPIService;

    private final PersonSerializer personSerializer;

    public PersonService(ActionNetworkAPIService actionNetworkAPIService, PersonSerializer personSerializer) {
        this.actionNetworkAPIService = actionNetworkAPIService;
        this.personSerializer = personSerializer;
    }



    public void importCSV(InputStream csvFileStream) throws JsonProcessingException {
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
                    .forEach(ActionNetworkAPIService::addPerson);
    }
}
