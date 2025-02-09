package org.raoulgrn.processors;

import org.raoulgrn.core.interfaces.DataProcessor;
import org.raoulgrn.core.models.Data;

public class WordCountProcessor implements DataProcessor {

    private int wordCount;

    @Override
    public void process(Data data) {

        if(data.getContent() == null || data.getContent().trim().isEmpty()){
            wordCount = 0;
            return;
        }

        wordCount = data.getContent().trim().split("\\s+").length;
    }

    @Override
    public Object getResult() {
        return wordCount;
    }

}
