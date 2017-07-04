package com.abhibus.intern.todo.todo;

/**
 * Created by Shaik Nazeer on 20-06-2017.
 */

public class ListModel {
    public String task_name;
    private String stime;
    private String etime;
    private String sdate;
    private String edate;
    private String addedOn;
    private String finish;

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }
    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }
    public String getAddedOn() {
        return addedOn;
    }
}
