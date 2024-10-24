package com.voxloud.provisioning.сonfig;

import lombok.Data;

import java.util.List;

@Data
public class DeviceConfig {
    private String username;
    private String password;
    private String domain;
    private String port;
    private Integer timeout;
    private List<String> codecs;
}
