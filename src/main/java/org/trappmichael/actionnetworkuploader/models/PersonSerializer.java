package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class PersonSerializer extends StdSerializer<Person> {

    public PersonSerializer() {
        this(null);
    }

    public PersonSerializer(final Class<Person> t) {
        super(t);
    }

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
