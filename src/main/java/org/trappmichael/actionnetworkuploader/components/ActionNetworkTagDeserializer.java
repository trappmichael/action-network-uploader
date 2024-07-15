package org.trappmichael.actionnetworkuploader.components;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;
import org.trappmichael.actionnetworkuploader.models.Tag;

import java.io.IOException;

@Component
public class ActionNetworkTagDeserializer extends StdDeserializer<Tag> {
    public ActionNetworkTagDeserializer() {
        this(null);
    }
    public ActionNetworkTagDeserializer(Class<?> vc) {
        super(vc);
    }

    // Receives json representing an Action Network entity in the Action Network API, extracts information
    // relevant for constructing an ActionNetworkEntity java object, and constructs and returns a new
    // ActionNetworkEntity java object
    @Override
    public Tag deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        // Maps nodes of the json representing an Action Network entity
        JsonNode actionNetworkTagsArrayNode = jp.getCodec().readTree(jp);

        // Extracts the Action Network entity title
        String tagName = actionNetworkTagsArrayNode.get("name").asText();

        return new Tag(tagName);
    }
}
