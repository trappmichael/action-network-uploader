package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.csv.CSVRecord;
import org.trappmichael.actionnetworkuploader.components.PersonSerializer;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonSerialize(using = PersonSerializer.class)
public class Person {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Tag> tags;

    public Person(String firstName, String lastName, String email, String phone, List<Tag> tags) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.tags = tags;
    }

    // Constructs and returns new Person java object from a single line of a .csv file
    public static Person parse(CSVRecord record) {
        String firstName = record.get("firstName");
        String lastName = record.get("lastName");
        String email = record.get("email");
        String phone = record.get("phone");
        String tags = record.get("tags");

        return new Person(firstName, lastName, email, phone, Tag.parse(tags));
    }
}
