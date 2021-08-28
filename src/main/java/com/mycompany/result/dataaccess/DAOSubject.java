/*
 * To change this Batch header, choose Batch Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.dataaccess;

import com.mysql.jdbc.Connection;
import com.mycompany.result.dataaccess.util.DAODataFillUtil;
import com.mycompany.result.dataobject.DOSubject;
import com.yohan.commons.filters.DOPropertyFilter;
import com.yohan.commons.filters.FilterUtil;
import com.yohan.mysql.db.IDbSession;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DatabaseException;
import com.yohan.exceptions.DoesNotExistException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author yohanr
 */
public class DAOSubject implements IDAOSubject {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void create(IDbSession dbSession, DOSubject subjectCreateRequest) throws CustomException {

        try {

            Connection dbs = (Connection) dbSession.get();

            String sql = "insert into subject (code,subject,no_of_credits,year,semester,result) values (?,?,?,?,?,?)";

            try (PreparedStatement preparedStatement = dbs.prepareStatement(sql)) {

                preparedStatement.setString(1, subjectCreateRequest.getCode());
                preparedStatement.setString(2, subjectCreateRequest.getSubject());
                preparedStatement.setInt(3, subjectCreateRequest.getNoOfCredits());
                preparedStatement.setInt(4, subjectCreateRequest.getYear());
                preparedStatement.setInt(5, subjectCreateRequest.getSemester());
                preparedStatement.setString(6, subjectCreateRequest.getResult());
                preparedStatement.execute();

                preparedStatement.close();

            } catch (SQLException ex) {

                logger.error(ex.getMessage());
                throw ex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
    }

    @Override
    public boolean update(IDbSession dbSession, DOSubject subjectUpdateRequest) throws CustomException {
        boolean isUpdated = false;
        try {

            Connection dbs = (Connection) dbSession.get();

            String sql = "update subject set subject = ?,no_of_credits=?, year=?,semester=?,result=? where code = ?";

            try (PreparedStatement preparedStatement = dbs.prepareStatement(sql)) {

                preparedStatement.setString(1, subjectUpdateRequest.getSubject());
                preparedStatement.setInt(2, subjectUpdateRequest.getNoOfCredits());
                preparedStatement.setInt(3, subjectUpdateRequest.getYear());
                preparedStatement.setInt(4, subjectUpdateRequest.getSemester());
                preparedStatement.setString(5, subjectUpdateRequest.getResult());
                preparedStatement.setString(6, subjectUpdateRequest.getCode());
                preparedStatement.execute();
                preparedStatement.executeUpdate();
                isUpdated = true;
                preparedStatement.close();

            } catch (SQLException ex) {

                logger.error(ex.getMessage());
                throw ex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }

        return isUpdated;
    }

    @Override
    public DOSubject get(IDbSession dbSession, String subjectCode) throws CustomException {
        try {

            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select * from subject where code ='" + subjectCode + "'";

            DOSubject subject = null;

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    subject = DAODataFillUtil.getSubject(rs);
                    break;
                }
                rs.close();
            }

            stmt.close();

            if (subject == null) {
                throw new DoesNotExistException("Subject code does not exist. Subject code " + subjectCode);
            }

            return subject;

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(IDbSession dbSession, String subjectCode) throws CustomException {

        boolean isDelete = false;
        try {

            Connection dbs = (Connection) dbSession.get();

            String sql = "delete from subject where code = ?";

            try (PreparedStatement preparedStatement = dbs.prepareStatement(sql)) {

                preparedStatement.setString(1, subjectCode);

                preparedStatement.executeUpdate();

                isDelete = true;

                preparedStatement.close();

            } catch (SQLException ex) {

                logger.error(ex.getMessage());
                throw ex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }

        return isDelete;

    }

    @Override
    public ArrayList<DOSubject> list(IDbSession dbSession, List<DOPropertyFilter> filterData, ArrayList<String> orderFields, boolean isDescending, int page, int limit) throws CustomException {
        DOSubject subject;
        ArrayList<DOSubject> list = new ArrayList<>();
        try {

            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select * from subject ";

            String whereClause = "";
            if (filterData != null && filterData.size() > 0) {
                whereClause = FilterUtil.generateWhereClause((ArrayList<DOPropertyFilter>) filterData);
            }

            if (!whereClause.isEmpty()) {
                sql = sql + "where " + whereClause;
            }

            if (orderFields != null && orderFields.size() > 0) {
                String orderStr = String.join(",", orderFields);
                sql = sql + "order by " + orderStr;
                if (isDescending) {
                    sql += " desc";
                }
            } else {
                sql = sql + "order by code";
                if (isDescending) {
                    sql += " desc";
                }
            }

            sql = sql + " limit " + limit + " offset " + ((page - 1) * limit);

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    subject = DAODataFillUtil.getSubject(rs);
                    list.add(subject);
                }
                rs.close();
            }
            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
        return list;
    }

    @Override
    public int count(IDbSession dbSession, List<DOPropertyFilter> filterData) throws CustomException {
        int count = 0;
        String whereClause = "";
        try {

            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select count(*) as count from subject ";

            if (filterData != null && filterData.size() > 0) {
                whereClause = FilterUtil.generateWhereClause((ArrayList<DOPropertyFilter>) filterData);
            }

            if (!whereClause.isEmpty()) {
                sql = sql + "where " + whereClause;
            }

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    count = rs.getInt("count");
                    break;
                }
                rs.close();
            }

            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
        return count;
    }

    @Override
    public boolean isExistsByCode(IDbSession dbSession, String subjectCode) {
        boolean isExist = false;

        try {
            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select * from subject where code ='" + subjectCode + "'";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    isExist = true;
                    break;
                }
                rs.close();
            }

            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return isExist;
    }


     public double gpa(IDbSession dbSession) throws CustomException {

        double gpa = 0.0;
        try {

            Connection dbs = (Connection) dbSession.get();

            String sql = "select sum(s.no_of_credits *g.gpa) / sum(s.no_of_credits) as gpa from subject s join grade g on s.result= g.grade ";

            try (PreparedStatement preparedStatement = dbs.prepareStatement(sql)) {


                try (ResultSet rs = preparedStatement.executeQuery(sql)) {
                while (rs.next()) {
                    gpa = rs.getDouble("gpa");
                    break;
                }
                rs.close();
            }


                preparedStatement.close();

            } catch (SQLException ex) {

                logger.error(ex.getMessage());
                throw ex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }

        return gpa;

    }
}
