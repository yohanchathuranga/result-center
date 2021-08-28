package com.mycompany.result.base;

import com.mycompany.result.dataaccess.IDAOFactory;
import com.mycompany.result.dataaccess.MySqlDbDAOFactory;
import com.yohan.commons.filters.DOPropertyFilter;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DoesNotExistException;
import com.yohan.exceptions.InvalidInputException;
import com.yohan.exceptions.UnknownException;
import com.yohan.mysql.db.IDbFactory;
import com.yohan.mysql.db.IDbSession;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.mycompany.result.dataaccess.IDAOSubject;
import com.mycompany.result.dataobject.DOListCountResult;
import com.mycompany.result.dataobject.DOSubject;
import com.mycompany.result.service.ISubjectManager;
import com.mycompany.result.util.InputValidatorUtil;

/**
 *
 * @author yohanr
 */
public class SubjectManager implements ISubjectManager {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    IDbFactory dbFactory;
    IDAOFactory daoFactory;
    IDAOSubject subjectDao;

    public SubjectManager(IDbFactory db) {
        this.dbFactory = db;
        daoFactory = new MySqlDbDAOFactory();
    }

    @Override
    public DOSubject create(DOSubject subjectCreateRequest) throws CustomException {

        IDbSession dbs = dbFactory.getNewDbSession();
        DOSubject subject = null;

        subjectDao = daoFactory.getSubjectDAO();

        try {

            String subjectCode = InputValidatorUtil.validateStringProperty("Subject Code", subjectCreateRequest.getCode());
            subjectCreateRequest.setCode(subjectCode);

            subjectDao.create(dbs, subjectCreateRequest);

            subject = subjectDao.get(dbs, subjectCode);

            return subject;

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }

    @Override
    public DOSubject update(DOSubject subjectUpdateRequest) throws CustomException {

        IDbSession dbs = dbFactory.getNewDbSession();
        DOSubject subject = null;
        subjectDao = daoFactory.getSubjectDAO();

        try {

            if (subjectUpdateRequest == null) {
                throw new InvalidInputException("Invalid Input object");
            }

            String subjectCode = InputValidatorUtil.validateStringProperty("Subject Code", subjectUpdateRequest.getCode());
            subjectUpdateRequest.setCode(subjectCode);

            if (!subjectDao.isExistsByCode(dbs, subjectCode)) {
                throw new DoesNotExistException("Subject code does not exists. code : " + subjectCode);
            }

            subjectDao.update(dbs, subjectUpdateRequest);

            subject = subjectDao.get(dbs, subjectCode);

            return subject;

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }

    @Override
    public DOSubject get(String subjectCode) throws CustomException {

        DOSubject subject = null;
        IDbSession dbs = dbFactory.getNewDbSession();

        subjectDao = daoFactory.getSubjectDAO();

        try {

            subjectCode = InputValidatorUtil.validateStringProperty("Subject code", subjectCode);

            if (!subjectDao.isExistsByCode(dbs, subjectCode)) {
                throw new DoesNotExistException("subject code does not exists. Subject code " + subjectCode);
            }

            subject = subjectDao.get(dbs, subjectCode);

            return subject;

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }

    @Override
    public boolean delete(String subjectCode) throws CustomException {

        IDbSession dbs = dbFactory.getNewDbSession();

        subjectDao = daoFactory.getSubjectDAO();

        try {

            subjectCode = InputValidatorUtil.validateStringProperty("Subject code", subjectCode);

            if (!subjectDao.isExistsByCode(dbs, subjectCode)) {
                throw new DoesNotExistException("Subject code does not exists. Subject code " + subjectCode);
            }
            return subjectDao.delete(dbs, subjectCode);

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }

    @Override
    public ArrayList<DOSubject> list(List<DOPropertyFilter> filterData, ArrayList<String> orderFields, boolean isDescending, int page, int limit) throws CustomException {

        IDbSession dbs = dbFactory.getNewDbSession();

        subjectDao = daoFactory.getSubjectDAO();

        try {

            if (page <= 0) {
                page = 1;
            }
            if (limit <= 0) {
                limit = 20;
            }

            return subjectDao.list(dbs, filterData, orderFields, isDescending, page, limit);

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }

    @Override
    public DOListCountResult count(List<DOPropertyFilter> filterData) throws CustomException {

        IDbSession dbs = dbFactory.getNewDbSession();

        subjectDao = daoFactory.getSubjectDAO();

        DOListCountResult countResult = new DOListCountResult();

        try {
            countResult.setCount(subjectDao.count(dbs, filterData));
            return countResult;

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }
    
     @Override
    public double gpa() throws CustomException {

        IDbSession dbs = dbFactory.getNewDbSession();

        subjectDao = daoFactory.getSubjectDAO();

        try {

            return subjectDao.gpa(dbs);

        } catch (CustomException ex) {
            logger.error(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new UnknownException(ex.getMessage());
        } finally {
            dbs.close();
        }
    }

}
