package com.example.oneessay;

import java.io.Serializable;

public class Essay implements Serializable {

    private String id;
    private String topic;

    public Essay(String id, String topic) {
        this.id = id;
        this.topic = topic;
    }

    public Essay()
    {
        id= "";
        topic = "";

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "id='" + id + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
