package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.trappmichael.actionnetworkuploader.components.PersonSerializer;

@Data
@JsonSerialize(using = PersonSerializer.class)
public class Person {

    private String firstName;
    private String lastName;
    private String email;

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Constructs and returns new Person java object from a single line of a .csv file
    public static Person parse(String csvLine) {
        String[] fields = csvLine.split(",\\s*");
        return new Person(fields[0],fields[1],fields[2]);
    }
}
