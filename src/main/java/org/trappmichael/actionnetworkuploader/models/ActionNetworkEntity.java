package org.trappmichael.actionnetworkuploader.models;

import lombok.Data;

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
