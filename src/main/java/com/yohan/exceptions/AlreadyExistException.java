package com.yohan.exceptions;

/**
 *
 * @author yohanr
 */
public class AlreadyExistException  extends CustomException{
    
    public AlreadyExistException() {
        super(ALREADY_EXIST, "Already Exist.", "AlreadyExist", "Already Exist");
    }
    
    public AlreadyExistException(String message) {
        super(ALREADY_EXIST, message, "AlreadyExist", "Already Exist");
    }
    
}
