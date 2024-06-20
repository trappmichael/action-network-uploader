package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ActionNetworkEntityDeserializer extends StdDeserializer<ActionNetworkEntity> {

    public ActionNetworkEntityDeserializer() {
        this(null);
    }
    public ActionNetworkEntityDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ActionNetworkEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode actionNetworkEntitiesArrayNode = jp.getCodec().readTree(jp);

        String type = null;

        if (actionNetworkEntitiesArrayNode.get("_links").get("self").get("href").asText().contains("/forms/")) {
            type = "forms";
        } else if (actionNetworkEntitiesArrayNode.get("_links").get("self").get("href").asText().contains("/events/")) {
            type = "events";
        } else if (actionNetworkEntitiesArrayNode.get("_links").get("self").get("href").asText().contains("/petitions/")) {
            type = "petitions";
        }

        String title = actionNetworkEntitiesArrayNode.get("title").asText();
        String endpoint = actionNetworkEntitiesArrayNode.get("identifiers").elements().next().asText().replaceAll("action_network:", "");

        return new ActionNetworkEntity(type,title,endpoint);
    }
}
