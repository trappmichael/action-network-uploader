package org.trappmichael.actionnetworkuploader.components;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;
import org.trappmichael.actionnetworkuploader.models.Person;

import java.io.IOException;
@Component
public class PersonSerializer extends StdSerializer<Person> {

    public PersonSerializer() {
        this(null);
    }

    public PersonSerializer(final Class<Person> t) {
        super(t);
    }

    // Takes in a Person java object and writes a json string to represent it that is
    // compatible with the Action Network API
    @Override
    public void serialize(Person person, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectFieldStart("person");
        jgen.writeStringField("family_name", person.getLastName());
        jgen.writeStringField("given_name", person.getFirstName());
        jgen.writeArrayFieldStart("email_addresses");
        jgen.writeStartObject();
        jgen.writeStringField("address", person.getEmail());
        jgen.writeStringField("status", "subscribed");
        jgen.writeEndObject();
        jgen.writeEndArray();
        jgen.writeEndObject();
        jgen.writeEndObject();
    }
}
