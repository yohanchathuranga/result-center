/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.dataaccess;


import com.mycompany.result.dataobject.DOSubject;
import com.yohan.commons.filters.DOPropertyFilter;
import com.yohan.mysql.db.IDbSession;
import com.yohan.exceptions.CustomException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yohanr
 */
public interface IDAOSubject {

    public void create(IDbSession dbSession, DOSubject subjectCreateRequest) throws CustomException;

    public boolean update(IDbSession dbSession, DOSubject subjectUpdateRequest) throws CustomException;

    public DOSubject get(IDbSession dbSession, String subjectCode) throws CustomException;

    public boolean delete(IDbSession dbSession, String subjectCode) throws CustomException;

    public ArrayList<DOSubject> list(IDbSession dbSession, List<DOPropertyFilter> filterData, ArrayList<String> orderFields, boolean isDescending, int page, int limit) throws CustomException;

    public int count(IDbSession dbSession, List<DOPropertyFilter> filterData) throws CustomException;

    public boolean isExistsByCode(IDbSession dbSession, String subjectCode);
    
    public double gpa(IDbSession dbSession) throws CustomException;

}
