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
public class DOCountRequest {

    ArrayList<DOPropertyFilter> filterData = new ArrayList<>();

    public ArrayList<DOPropertyFilter> getFilterData() {
        return filterData;
    }

    public void setFilterData(ArrayList<DOPropertyFilter> filterData) {
        this.filterData = filterData;
    }

}
