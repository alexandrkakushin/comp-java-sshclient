package ru.ak.model;

import javax.xml.bind.annotation.XmlElement;

public class Response {

	private Object object;
	private boolean error;
	private String description;

    public Response() {}

    public Response(Object object, boolean error, String description) {
        this();
        this.object = object;
        this.error = error;
        this.description = description;
    }

    @XmlElement
    public Object getResult() {
        return object;
    }

    public void setResult(Object object) {
        this.object = object;
    }

    @XmlElement
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}