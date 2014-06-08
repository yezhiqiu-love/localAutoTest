package com.junqing.qa.exception;

import java.io.IOException;

public class MongodbLibraryException extends Exception {
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MongodbLibraryException(String msg, IOException e) {
        super(msg);
    }
}
