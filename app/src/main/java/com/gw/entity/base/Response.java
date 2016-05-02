package com.gw.entity.base;

/**
 * Created by gongwen on 2016/4/24.
 */
public class Response<T> {
    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean isSuccess() {
        return error == false ? true : false;
    }
}
