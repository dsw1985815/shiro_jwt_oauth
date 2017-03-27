package com.dongsw.authority.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "audience", locations = "classpath:jwt.properties")
public class Audience {
    private String base64Secret;
    private String name;
    private int defaultCheckTimes;
    private int defaultExpire;

    public String getName() {
        return name;
    }

    public int getDefaultCheckTimes() {
        return defaultCheckTimes;
    }

    public void setDefaultCheckTimes(int defaultCheckTimes) {
        this.defaultCheckTimes = defaultCheckTimes;
    }

    public int getDefaultExpire() {
        return defaultExpire;
    }

    public void setDefaultExpire(int defaultExpire) {
        this.defaultExpire = defaultExpire;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

}
