package com.resolutions.application.ports.out;

public interface LoggerLocal {
    void info(String msg);
    void debug(String msg);
    void warning(String msg);
    void error(String msg);
}
