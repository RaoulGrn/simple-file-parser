package org.raoulgrn.core.interfaces;

import org.raoulgrn.core.models.Data;

public interface DataProcessor {
    void process(Data data);
    Object getResult();
}
