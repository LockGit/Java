package com.lock.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lock
 * @date 2018/4/30 17:24
 */
public class SimpleSessionParam {
    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("app_version")
    private String appVersion;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
