package org.trappmichael.actionnetworkuploader.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class Person {

    private String firstName;
    private String lastName;
    private String email;

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static Person parse(String csvLine) {
        String[] fields = csvLine.split(",\\s*");
        return new Person(fields[0],fields[1],fields[2]);
    }
}
