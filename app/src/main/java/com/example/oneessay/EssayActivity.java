package com.example.oneessay;

import java.io.Serializable;
import java.util.ArrayList;

public class EssayActivity implements Serializable {

    private String id;
    private String essaytopic;
    private String essaycontent;
    private String currentstudent;
    private ArrayList<String> nextstudents;
    private Boolean status;
    private String time;

    public EssayActivity()
    {
        this.id = "";
        this.essaytopic = "";
        this.essaycontent = "";
        this.currentstudent = "";
        this.status = Boolean.TRUE;
        this.nextstudents = new ArrayList<String>();
        this.time = "";
    }

    public EssayActivity(String id, String essaytopic, String essaycontent, String currentstudent, ArrayList<String> nextstudent, Boolean status, String time) {
        this.id = id;
        this.essaytopic = essaytopic;
        this.essaycontent = essaycontent;
        this.currentstudent = currentstudent;
        this.nextstudents = nextstudent;
        this.status = status;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEssaytopic() {
        return essaytopic;
    }

    public void setEssaytopic(String essaytopic) {
        this.essaytopic = essaytopic;
    }

    public String getEssaycontent() {
        return essaycontent;
    }

    public void setEssaycontent(String essaycontent) {
        this.essaycontent = essaycontent;
    }

    public String getCurrentstudent() {
        return currentstudent;
    }

    public void setCurrentstudent(String currentstudent) {
        this.currentstudent = currentstudent;
    }

    public ArrayList<String> getNextstudents() {
        return nextstudents;
    }

    public void setNextstudents(ArrayList<String> nextstudents) {
        this.nextstudents = nextstudents;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "EssayActivity{" +
                "id='" + id + '\'' +
                ", essaytopic='" + essaytopic + '\'' +
                ", essaycontent='" + essaycontent + '\'' +
                ", currentstudent='" + currentstudent + '\'' +
                ", nextstudents=" + nextstudents.toString() +
                ", status=" + status +
                ", time=" + time +
                '}';
    }
}



