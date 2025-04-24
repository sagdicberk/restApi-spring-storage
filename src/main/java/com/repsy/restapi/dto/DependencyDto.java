package com.repsy.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DependencyDto {
    @JsonProperty("package")
    private String packageName;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
