/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.dataaccess.util;

import com.mycompany.result.dataobject.DOSubject;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author yohanr
 */
public class DAODataFillUtil {

    
    public static DOSubject getSubject(ResultSet rSubject) throws SQLException {

        DOSubject subject= new DOSubject();
        subject.setCode(rSubject.getString("code"));
        subject.setSubject(rSubject.getString("subject"));
        subject.setNoOfCredits(rSubject.getInt("no_of_credits"));
        subject.setYear(rSubject.getInt("year"));
        subject.setSemester(rSubject.getInt("semester"));
        subject.setResult(rSubject.getString("result"));
        return subject;
    }
}
