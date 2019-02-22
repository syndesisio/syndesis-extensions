package io.syndesis.extension.cache;

public class HelloBean {
    public static int counter = 0;

    public String work(String body){
        return String.format("Hello %d: %s", counter++, body);
    }
}
