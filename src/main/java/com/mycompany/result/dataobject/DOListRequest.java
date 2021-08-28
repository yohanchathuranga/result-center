/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.dataobject;

import com.yohan.commons.filters.DOPropertyFilter;
import java.util.ArrayList;

/**
 *
 * @author yohanr
 */
public class DOListRequest {

    private int limit;
    private int page;
    boolean descending;
    ArrayList<DOPropertyFilter> filterData = new ArrayList<>();
    ArrayList<String> orderFields = new ArrayList<>();

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    public ArrayList<DOPropertyFilter> getFilterData() {
        return filterData;
    }

    public void setFilterData(ArrayList<DOPropertyFilter> filterData) {
        this.filterData = filterData;
    }

    public ArrayList<String> getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(ArrayList<String> orderFields) {
        this.orderFields = orderFields;
    }

}
