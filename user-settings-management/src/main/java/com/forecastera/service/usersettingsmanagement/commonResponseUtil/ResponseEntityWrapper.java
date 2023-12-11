package com.forecastera.service.usersettingsmanagement.commonResponseUtil;
/*
 * @Author Kanishk Vats
 * @Create 06-09-2023
 * @Description
 */

public class ResponseEntityWrapper {
    private final Object data;

    public ResponseEntityWrapper(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
