package com.metacoder.transalvania.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BudgetModel implements Serializable {
    String id, total , from , tol   , title ;
    int upperLimit , lowerLimit ;
    List<CalacModel> breakDowns = new ArrayList<>() ;

    public BudgetModel() {
    }

    public BudgetModel(String id, String total, String from, String tol, String title, int upperLimit, int lowerLimit, List<CalacModel> breakDowns) {
        this.id = id;
        this.total = total;
        this.from = from;
        this.tol = tol;
        this.title = title;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.breakDowns = breakDowns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CalacModel> getBreakDowns() {
        return breakDowns;
    }

    public void setBreakDowns(List<CalacModel> breakDowns) {
        this.breakDowns = breakDowns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFrom() {
        return from;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
}
