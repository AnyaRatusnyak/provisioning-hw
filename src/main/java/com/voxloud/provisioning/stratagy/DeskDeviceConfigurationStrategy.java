package com.voxloud.provisioning.stratagy;

import com.voxloud.provisioning.annotation.DeviceModelSupported;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.сonfig.DeviceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@DeviceModelSupported(Device.DeviceModel.DESK)
@Component
public class DeskDeviceConfigurationStrategy implements DeviceConfigurationStrategy {
    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private int port;

    @Value("${provisioning.codecs}")
    private String codecs;

    @Override
    public String generateConfiguration(Device device) {
        DeviceConfig config = new DeviceConfig();
        config.setUsername(device.getUsername());
        config.setPassword(device.getPassword());

        String effectiveDomain = domain;
        String effectiveTimeout = null;
        String effectivePort = String.valueOf(port);
        List<String> effectiveCodecs = Arrays.asList(codecs.split(","));

        if (device.getOverrideFragment() != null) {
            String[] parts = device.getOverrideFragment().split("\n");
            for (String part : parts) {
                String[] keyValue = part.split("=");
                if (keyValue.length == 2) {
                    switch (keyValue[0]) {
                        case "domain":
                            effectiveDomain = keyValue[1];
                            break;
                        case "port":
                            effectivePort = keyValue[1];
                            break;
                        case "codecs":
                            effectiveCodecs = Arrays.asList(keyValue[1].split(","));
                            break;
                        case "timeout":
                            effectiveTimeout = keyValue[1];
                            break;
                    }
                }
            }
        }

        config.setDomain(effectiveDomain);
        config.setPort(effectivePort);
        config.setCodecs(effectiveCodecs);
        if (effectiveTimeout != null) {
            config.setTimeout(Integer.valueOf(effectiveTimeout));
        }
        return buildConfigurationString(config);
    }

    private String buildConfigurationString(DeviceConfig config) {
        StringBuilder configString = new StringBuilder();
        configString.append("username=").append(config.getUsername()).append("\n");
        configString.append("password=").append(config.getPassword()).append("\n");
        configString.append("domain=").append(config.getDomain()).append("\n");
        configString.append("port=").append(config.getPort()).append("\n");
        configString.append("codecs=").append(String.join(",", config.getCodecs())).append("\n");

        if (config.getTimeout() != null) {
            configString.append("timeout=").append(config.getTimeout()).append("\n");
        }
        return configString.toString();
    }
}
