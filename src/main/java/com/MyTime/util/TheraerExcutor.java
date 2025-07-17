package com.MyTime.util;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component("customExecutor")
public class TheraerExcutor {
    public AsyncTaskExecutor customExecutor() {
        return (AsyncTaskExecutor) (AsyncTaskExecutor) Executors.newSingleThreadExecutor();
    }

}
