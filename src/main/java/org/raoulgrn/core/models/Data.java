package org.raoulgrn.core.models;

import java.util.Map;

public class Data {
    private String content;
    private Map<String, Object> metadata;

    public Data(String content, Map<String, Object> metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "Data{" +
                "content='" + content + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
