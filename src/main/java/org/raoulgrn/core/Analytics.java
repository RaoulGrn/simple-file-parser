package org.raoulgrn.core;

import org.raoulgrn.core.interfaces.DataProcessor;
import org.raoulgrn.core.interfaces.DataSource;
import org.raoulgrn.core.models.AnalyticsResult;
import org.raoulgrn.core.models.Data;

import java.util.List;

public class Analytics {
    private List<DataProcessor> processors;
    private AnalyticsResult analyticsResult;

    public Analytics() {
    }

    public Analytics(List<DataProcessor> processors, AnalyticsResult analyticsResult) {
        this.processors = processors;
        this.analyticsResult = analyticsResult;
    }

    public AnalyticsResult processData(DataSource source) {
        Data data = source.getData();
        analyticsResult.clearResults();

        for(DataProcessor processor: processors){
            processor.process(data);
            String processorName = processor.getClass().getSimpleName();
            analyticsResult.addResult(processorName, processor.getResult());
        }

        return getAnalyticsResult();
    }

    public void addProcessor (DataProcessor processor){
        processors.add(processor);
    }

    public void removeProcessor (DataProcessor processor) {
        processors.remove(processor);
    }

    public AnalyticsResult getAnalyticsResult() {
        return analyticsResult;
    }

    public void setProcessors(List<DataProcessor> processors) {
        this.processors = processors;
    }

    public void setAnalyticsResult(AnalyticsResult analyticsResult) {
        this.analyticsResult = analyticsResult;
    }
}
