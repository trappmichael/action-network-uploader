package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.trappmichael.actionnetworkuploader.components.ActionNetworkEntityDeserializer;

@Data
@JsonDeserialize(using = ActionNetworkEntityDeserializer.class)
public class ActionNetworkEntity {
    String type;
    String title;
    String endpoint;

    public ActionNetworkEntity() {}

    public ActionNetworkEntity(String type, String title, String endpoint) {
        this.type = type;
        this.title = title;
        this.endpoint = endpoint;
    }
}
