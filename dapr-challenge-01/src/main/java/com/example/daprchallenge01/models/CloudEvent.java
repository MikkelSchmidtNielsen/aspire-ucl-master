package com.example.daprchallenge01.models;

public class CloudEvent<T> {
    private String type;
    private String source;
    private T data;

    public CloudEvent(String type, String source, T data) {
        this.type = type;
        this.source = source;
        this.data = data;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
