package org.innowise.testProject.handler;

import lombok.extern.slf4j.Slf4j;
import org.innowise.testProject.exception.EntityAlreadyExistException;
import org.innowise.testProject.exception.NoDataFoundException;
import java.sql.SQLException;

@Slf4j
public class ErrorHandler {
    public static String resolveErrorMessage(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return "Invalid input: " + e.getMessage();
        } else if (e instanceof NoDataFoundException) {
            return "No data found: " + e.getMessage();
        } else if (e instanceof EntityAlreadyExistException) {
            return e.getMessage();
        } else if (e instanceof SQLException || e instanceof RuntimeException) {
            return e.getMessage();
        } else {
            return "An unexpected error occurred.";
        }
    }
}
