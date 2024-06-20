package org.trappmichael.actionnetworkuploader.components;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;
import org.trappmichael.actionnetworkuploader.models.ActionNetworkEntity;

import java.io.IOException;

@Component
public class ActionNetworkEntityDeserializer extends StdDeserializer<ActionNetworkEntity> {

    public ActionNetworkEntityDeserializer() {
        this(null);
    }
    public ActionNetworkEntityDeserializer(Class<?> vc) {
        super(vc);
    }

    // Receives json representing an Action Network entity in the Action Network API, extracts information
    // relevant for constructing an ActionNetworkEntity java object, and constructs and returns a new
    // ActionNetworkEntity java object
    @Override
    public ActionNetworkEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        // Maps nodes of the json representing an Action Network entity
        JsonNode actionNetworkEntitiesArrayNode = jp.getCodec().readTree(jp);

        String type = null;

        // Extracts the Action Network entity type (i.e., form, event, or petition)
        if (actionNetworkEntitiesArrayNode.get("_links").get("self").get("href").asText().contains("/forms/")) {
            type = "forms";
        } else if (actionNetworkEntitiesArrayNode.get("_links").get("self").get("href").asText().contains("/events/")) {
            type = "events";
        } else if (actionNetworkEntitiesArrayNode.get("_links").get("self").get("href").asText().contains("/petitions/")) {
            type = "petitions";
        }

        // Extracts the Action Network entity title
        String title = actionNetworkEntitiesArrayNode.get("title").asText();

        // Extracts the Action Network entity endpoint
        String endpoint = actionNetworkEntitiesArrayNode.get("identifiers").elements().next().asText().replaceAll("action_network:", "");

        // Constructs and returns a new ActionNetworkEntity java object using the extracted data
        return new ActionNetworkEntity(type,title,endpoint);
    }
}
