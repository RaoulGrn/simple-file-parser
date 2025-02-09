package org.raoulgrn;

import org.raoulgrn.core.Analytics;
import org.raoulgrn.core.interfaces.DataProcessor;
import org.raoulgrn.core.interfaces.FileType;
import org.raoulgrn.core.models.AnalyticsResult;
import org.raoulgrn.core.monitor.Monitor;
import org.raoulgrn.parsers.TextParser;
import org.raoulgrn.processors.DotCountProcessor;
import org.raoulgrn.processors.MostUsedWordProcessor;
import org.raoulgrn.processors.WordCountProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Main {
    public static void main(String[] args) {

    try {

        List<DataProcessor> processors = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        AnalyticsResult analyticsResult = new AnalyticsResult(result);

        Analytics analytics = new Analytics(processors, analyticsResult);

        analytics.addProcessor(new WordCountProcessor());
        analytics.addProcessor(new DotCountProcessor());
        analytics.addProcessor(new MostUsedWordProcessor());

        FileType textParser = new TextParser();

        Monitor monitor = new Monitor(analytics, textParser);

        monitor.startMonitor();

    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }








    }
}