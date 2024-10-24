package com.voxloud.provisioning.stratagy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.annotation.DeviceModelSupported;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.JsonConversionException;
import com.voxloud.provisioning.exception.JsonParseException;
import com.voxloud.provisioning.сonfig.DeviceConfig;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
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

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

    @Override
    public String generateConfiguration(Device device) {
        DeviceConfig config = new DeviceConfig();
        config.setUsername(device.getUsername());
        config.setPassword(device.getPassword());
        config.setDomain(getOverrideOrDefault(device, "domain", domain));
        config.setPort(getOverrideOrDefault(device, "port", String.valueOf(port)));
        config.setCodecs(Arrays.asList(codecs.split(",")));

        if (device.getOverrideFragment() != null) {
            Map<String, Object> overrides = parseJsonOverrideFragment(device.getOverrideFragment());
            if (overrides.containsKey("domain")) config.setDomain((String) overrides.get("domain"));
            if (overrides.containsKey("port")) config.setPort((String) overrides.get("port"));
            if (overrides.containsKey("timeout"))
                config.setTimeout((Integer) overrides.get("timeout"));
            if (overrides.containsKey("codecs"))
                config.setCodecs(Arrays.asList(((String) overrides.get("codecs")).split(",")));
        }

        return convertConfigToJson(config);
    }

    private String getOverrideOrDefault(Device device, String key, String defaultValue) {
        if (device.getOverrideFragment() != null) {
            Map<String, Object> overrides = parseJsonOverrideFragment(device.getOverrideFragment());
            if (overrides.containsKey(key)) {
                return (String) overrides.get(key);
            }
        }
        return defaultValue;
    }

    private Map<String, Object> parseJsonOverrideFragment(String overrideFragment) {
        try {
            return objectMapper.readValue(overrideFragment, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new JsonParseException("Failed to parse override fragment", e);
        }
    }

    private String convertConfigToJson(DeviceConfig config) {
        try {
            return objectMapper.writeValueAsString(config);
        } catch (IOException e) {
            throw new JsonConversionException("Failed to convert DeviceConfig to JSON", e);
        }
    }
}
