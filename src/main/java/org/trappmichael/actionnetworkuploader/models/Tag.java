package org.trappmichael.actionnetworkuploader.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.trappmichael.actionnetworkuploader.components.ActionNetworkTagDeserializer;

import java.util.Arrays;
import java.util.List;

@Data
@JsonDeserialize(using = ActionNetworkTagDeserializer.class)
public class Tag {

    String name;

    public Tag(String name) {
        this.name = name;
    }

    public static List<Tag> parse(String tagCell) {
        return Arrays.stream(tagCell.split(",\\s*"))
                .map(Tag::new)
                .toList();
    }
}
