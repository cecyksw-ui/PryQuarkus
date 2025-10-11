package com.resolutions.adapters.out.logger;

import com.resolutions.application.ports.out.LoggerLocal;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LoggerImp implements LoggerLocal {
    private Logger logger;

    public LoggerImp(Logger log) {
        this.logger = log;
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void warning(String msg) {
        logger.warn(msg);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }
}
