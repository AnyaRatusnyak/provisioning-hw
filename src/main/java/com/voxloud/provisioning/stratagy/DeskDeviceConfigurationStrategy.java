package com.voxloud.provisioning.stratagy;

import com.voxloud.provisioning.annotation.DeviceModelSupported;
import com.voxloud.provisioning.entity.Device;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        StringBuilder config = new StringBuilder();

        config.append("username=").append(device.getUsername()).append("\n");
        config.append("password=").append(device.getPassword()).append("\n");

        if (device.getOverrideFragment() != null) {
            String[] parts = device.getOverrideFragment().split(" ");
            for (String part : parts) {
                config.append(part).append("\n");
            }
        }

        if (!config.toString().contains("domain=")) {
            config.append("domain=").append(domain).append("\n");
        }
        if (!config.toString().contains("port=")) {
            config.append("port=").append(port).append("\n");
        }
        if (!config.toString().contains("codecs=")) {
            config.append("codecs=").append(codecs).append("\n");
        }

        return config.toString();
    }
}
