package com.project.emailSchedular.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordNotFoundException extends Exception {
    Logger log = LoggerFactory.getLogger(RecordNotFoundException.class);
    public RecordNotFoundException(String s) {
        log.error(s);
    }

}
