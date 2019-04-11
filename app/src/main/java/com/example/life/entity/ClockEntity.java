package com.example.life.entity;

import java.util.Date;

public class ClockEntity {


    private long time;
    private String id;
    private String desc;
    private Integer status;
    private Integer cycle;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getCycle() {
        return cycle;
    }
}
