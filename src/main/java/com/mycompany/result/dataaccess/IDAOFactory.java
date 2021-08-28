/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.dataaccess;

import com.yohan.mysql.db.IDbFactory;

/**
 *
 * @author yohanr
 */
public interface IDAOFactory {

    public void setDbFactory(IDbFactory dbFactory);

    public IDbFactory getDbFactorty();

    public IDAOSubject getSubjectDAO();

}
