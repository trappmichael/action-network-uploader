package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Map;

@Data
public class ActionNetworkEntity {
    String type;
    String title;
    String endpoint;

    public ActionNetworkEntity(String type, String title, String endpoint) {
        this.type = type;
        this.title = title;
        this.endpoint = endpoint;
    }
}
