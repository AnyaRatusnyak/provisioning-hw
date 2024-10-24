package com.voxloud.provisioning.stratagy;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConferenceDeviceConfigurationStrategyTest {
    @InjectMocks
    private ConferenceDeviceConfigurationStrategy strategy;

    private static final String DOMAIN = "sip.voxloud.com";
    private static final int PORT = 5060;
    private static final String CODECS = "G711,G729";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "pass";
    private static final String OVERRIDE_FRAGMENT = "{\"domain\":\"override.com\",\"port\":\"9090\"}";
    private static final String INVALID_FRAGMENT = "invalid json";
    private Device device;

    @BeforeEach
    void setUp() {
        strategy = new ConferenceDeviceConfigurationStrategy();
        ReflectionTestUtils.setField(strategy, "domain", DOMAIN);
        ReflectionTestUtils.setField(strategy, "port", PORT);
        ReflectionTestUtils.setField(strategy, "codecs", CODECS);
        device = new Device();
        device.setUsername(USER_NAME);
        device.setPassword(PASSWORD);
    }

    @Test
    @DisplayName("Generate device configuration without override fragment")
    void generateConfiguration_WithoutOverrideFragment_ReturnsJson() throws IOException {
        device.setOverrideFragment(null);

        String result = strategy.generateConfiguration(device);

        String expectedJson = "{\"username\":\"user\",\"password\":\"pass\",\"domain\":\"sip.voxloud.com\",\"port\":\"5060\",\"codecs\":[\"G711\",\"G729\"]}";

        assertEquals(expectedJson, result);
    }

    @Test
    @DisplayName("Generate device configuration with override fragment")
    void generateConfiguration_WithOverrideFragment_ReturnsJson() throws IOException {
        device.setOverrideFragment(OVERRIDE_FRAGMENT);

        String result = strategy.generateConfiguration(device);
        String expectedJson = "{\"username\":\"user\",\"password\":\"pass\",\"domain\":\"override.com\",\"port\":\"9090\",\"codecs\":[\"G711\",\"G729\"]}";
        assertEquals(expectedJson, result);
    }

    @Test
    @DisplayName("Verify generateConfiguration() method throws exception for invalid data fragment")
    void generateConfiguration_WithInvalidOverrideFragment_ThrowsException() {
        device.setOverrideFragment(INVALID_FRAGMENT);

        assertThrows(JsonParseException.class, () -> strategy.generateConfiguration(device));
    }
}
