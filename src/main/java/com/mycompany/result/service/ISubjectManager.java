/*
 * To change this jig header, choose Jig Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.service;

import com.mycompany.result.dataobject.DOListCountResult;
import com.mycompany.result.dataobject.DOSubject;
import com.yohan.commons.filters.DOPropertyFilter;
import com.yohan.exceptions.CustomException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yohanr
 */
public interface ISubjectManager {

    public DOSubject create(DOSubject subjectCreateRequest) throws CustomException;

    public DOSubject update(DOSubject subjectUpdateRequest) throws CustomException;

    public DOSubject get(String subjectId) throws CustomException;

    public boolean delete(String subjectId) throws CustomException;

    public ArrayList<DOSubject> list(List<DOPropertyFilter> filterData, ArrayList<String> orderFields, boolean isDescending, int page, int limit) throws CustomException;

    public DOListCountResult count(List<DOPropertyFilter> filterData) throws CustomException;
    
    public double gpa() throws CustomException;

}
