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
public class MySqlDbDAOFactory implements IDAOFactory {

    IDbFactory dbFactory;

    @Override
    public void setDbFactory(IDbFactory dbFactory) {
        this.dbFactory = dbFactory;
    }

    @Override
    public IDbFactory getDbFactorty() {
        return dbFactory;
    }

    @Override
    public IDAOSubject getSubjectDAO() {
        return new DAOSubject();
    }

}
