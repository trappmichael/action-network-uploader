package org.trappmichael.actionnetworkuploader.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.trappmichael.actionnetworkuploader.models.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    private final ActionNetworkAPIService actionNetworkAPIService;

    public PersonService(ActionNetworkAPIService actionNetworkAPIService) {
        this.actionNetworkAPIService = actionNetworkAPIService;
    }

    // Imports a .csv file of person records to an Action Network API endpoint by reading the .csv file
    // one line at a time, parsing each line into a Person java object, serializing each Person java object
    // into a json string compatible with the Action Network API, and uploading each person to the API.
    public void importCSV(String actionNetworkEntityType, String actionNetworkEntityApiEndpoint, InputStream csvFileStream) throws IOException {

        Reader reader = new InputStreamReader(csvFileStream);

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("firstName", "lastName", "email", "tags")
                .setSkipHeaderRecord(true)
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(reader);

        for (CSVRecord record : records) {

            Person person = Person.parse(record);
            String serializedPerson = new ObjectMapper().writeValueAsString(person);

            actionNetworkAPIService.addPerson(serializedPerson, actionNetworkEntityType, actionNetworkEntityApiEndpoint);
            System.out.println(serializedPerson);
        }
    }

//        InputStreamReader inputStreamReader = new InputStreamReader(csvFileStream);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//        bufferedReader.lines()
//                    .skip(1)
//                    .limit(500)
//                    .map(Person::parse)
//                    .map(person -> {
//                        try {
//                            return new ObjectMapper().writeValueAsString(person);
//                        } catch (JsonProcessingException e) {
//                            throw new RuntimeException(e);
//                        }
//                    })
//                    .forEach(serializedPerson -> {
//                        System.out.println(serializedPerson);
//                        actionNetworkAPIService.addPerson(serializedPerson, actionNetworkEntityType, actionNetworkEntityApiEndpoint);
//                    });
//    }
}
