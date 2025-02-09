package org.raoulgrn.core.models;

import java.util.Map;

public class AnalyticsResult {
    private Map<String, Object> result;

    public AnalyticsResult(Map<String, Object> result) {
        this.result = result;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public void addResult(String key, Object value) {
        result.put(key, value);
    }

    public void clearResults(){
        result.clear();
    }

    @Override
    public String toString() {
        return "AnalyticsResult{" +
                "result=" + result +
                '}';
    }
}
