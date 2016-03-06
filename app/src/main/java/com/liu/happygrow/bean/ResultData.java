package com.liu.happygrow.bean;

import java.util.List;

/**
 * Created by Liu on 2016/3/6.
 */
public class ResultData {
    private String error;
    private List<GanHuo> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<GanHuo> getResults() {
        return results;
    }

    public void setResults(List<GanHuo> results) {
        this.results = results;
    }
}
