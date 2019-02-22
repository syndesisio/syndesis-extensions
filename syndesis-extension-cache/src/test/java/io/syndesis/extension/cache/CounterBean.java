package io.syndesis.extension.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheActionTest.class);
    public static int counter = 0;

    public String work(String body){
        return String.format("%d: %s", counter++, body);
    }
}
