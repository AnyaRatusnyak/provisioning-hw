package com.voxloud.provisioning.stratagy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.annotation.DeviceModelSupported;
import com.voxloud.provisioning.entity.Device;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@DeviceModelSupported(Device.DeviceModel.CONFERENCE)
@Component
@Setter
public class ConferenceDeviceConfigurationStrategy implements DeviceConfigurationStrategy {

    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private int port;

    @Value("${provisioning.codecs}")
    private String codecs;

    @Override
    public String generateConfiguration(Device device) {
        Map<String, Object> config = new HashMap<>();
        config.put("username", device.getUsername());
        config.put("password", device.getPassword());
        config.put("domain", getOverrideOrDefault(device, "domain", domain));
        config.put("port", getOverrideOrDefault(device, "port", String.valueOf(port)));
        config.put("codecs", codecs.split(","));

        if (device.getOverrideFragment() != null) {
            config.putAll(parseJsonOverrideFragment(device.getOverrideFragment()));
        }

        try {
            return new ObjectMapper().writeValueAsString(config);
        } catch (IOException e) {
            throw new RuntimeException("Error generating JSON configuration", e);
        }
    }

    private String getOverrideOrDefault(Device device, String key, String defaultValue) {
        if (device.getOverrideFragment() != null && device.getOverrideFragment().contains(key)) {
            return device.getOverrideFragment();
        }
        return defaultValue;
    }

    private Map<String, Object> parseJsonOverrideFragment(String overrideFragment) {
        try {
            return new ObjectMapper().readValue(overrideFragment, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse override fragment", e);
        }
    }

}
