package com.github.traverser;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TraverserLogger {
    public static void warning(Class<?> target, String message, Exception thrown){
        Logger logger = Logger.getLogger(target.getName());
        logger.log(Level.WARNING, message, thrown);
    }
}
