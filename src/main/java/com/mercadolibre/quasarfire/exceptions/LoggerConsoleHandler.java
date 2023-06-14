package com.mercadolibre.quasarfire.exceptions;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class LoggerConsoleHandler extends StreamHandler {
    @Override
    public void publish(LogRecord logRecord) {
        super.publish(logRecord);
    }

    @Override
    public void flush() {
        super.flush();
    }


    @Override
    public void close() throws SecurityException {
        super.close();
    }

}
