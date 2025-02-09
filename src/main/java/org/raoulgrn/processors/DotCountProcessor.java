package org.raoulgrn.processors;

import org.raoulgrn.core.interfaces.DataProcessor;
import org.raoulgrn.core.models.Data;

public class DotCountProcessor implements DataProcessor {
    private int dotCount;

    @Override
    public void process(Data data) {
        dotCount = (int) data.getContent()
                .chars()
                .filter(character -> character == '.')
                .count();
    }

    @Override
    public Object getResult() {
        return dotCount;
    }
}
