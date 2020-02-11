package com.howtodoinjava.demo.config;

import java.io.FileNotFoundException;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.dao.DuplicateKeyException;

public class CustomSkipPolicy implements SkipPolicy {

    private static final int MAX_SKIP_COUNT = 5;

    @Override
    public boolean shouldSkip(Throwable throwable, int skipCount) throws SkipLimitExceededException {

        if (skipCount < MAX_SKIP_COUNT) {
            if ((throwable instanceof FileNotFoundException) 
            || (throwable instanceof FlatFileParseException)
            || (throwable instanceof NumberFormatException)
            || (throwable instanceof StringIndexOutOfBoundsException)
            || (throwable instanceof ArithmeticException) 
            || (throwable instanceof DuplicateKeyException) ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}