package org.raoulgrn.core.interfaces;

import org.raoulgrn.core.models.Data;

import java.util.Map;

public interface DataSource {
    Data getData();
    Map<String, Object> getMetadata();
}
