package org.raoulgrn.processors;

import org.raoulgrn.core.interfaces.DataProcessor;
import org.raoulgrn.core.models.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MostUsedWordProcessor implements DataProcessor {

    private Map<String, Object> result;


    @Override
    public void process(Data data) {

        if (data.getContent() == null || data.getContent().isEmpty()) {
            result = new HashMap<>();
            result.put("word", "");
            result.put("count", 0L);
            return;
        }

        Map<String, Long> frequency = Arrays.stream(data.getContent()
                        .toLowerCase()
                        .replaceAll("[^a-z\\s]"," ")
                        .split("\\W+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()));

        if(frequency.isEmpty()){
            result = new HashMap<>();
            result.put("word", "");
            result.put("count", 0L);
            return;
        }

        Map.Entry<String, Long> mostUsedWord = frequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        result = new HashMap<>();
        result.put("word", mostUsedWord.getKey());
        result.put("count", mostUsedWord.getValue());

    }

    @Override
    public Object getResult() {
        return result;
    }


}
