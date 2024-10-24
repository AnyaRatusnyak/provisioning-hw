package com.voxloud.provisioning.stratagy;

import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeskDeviceConfigurationStrategyTest {
    @InjectMocks
    private DeskDeviceConfigurationStrategy strategy;

    private Device device;

    private static final String DOMAIN = "sip.voxloud.com";
    private static final int PORT = 5060;
    private static final String CODECS = "G711,G729";
    private static final String USER_NAME = "user";
    private static final String PASSWORD = "pass";
    private static final String OVERRIDE_FRAGMENT = "domain=override.com port=9090 codecs=АА12 timeout=10";
    private static final String PARTIAL_OVERRIDE_FRAGMENT = "port=9090";

    @BeforeEach
    void setUp() {
        strategy = new DeskDeviceConfigurationStrategy();
        ReflectionTestUtils.setField(strategy, "domain", DOMAIN);
        ReflectionTestUtils.setField(strategy, "port", PORT);
        ReflectionTestUtils.setField(strategy, "codecs", CODECS);
        device = new Device();
        device.setUsername(USER_NAME);
        device.setPassword(PASSWORD);
    }

    @Test
    @DisplayName("Generate device configuration without override fragment")
    void generateConfiguration_WithoutOverrideFragment_ReturnsConfig() {
        device.setOverrideFragment(null);

        String result = strategy.generateConfiguration(device);

        String expectedConfig = "username=user\n" +
                "password=pass\n" +
                "domain=sip.voxloud.com\n" +
                "port=5060\n" +
                "codecs=G711,G729\n";

        assertEquals(expectedConfig, result);
    }

    @Test
    @DisplayName("Generate device configuration with override fragment")
    void generateConfiguration_WithOverrideFragment_ReturnsConfig() {
        device.setOverrideFragment(OVERRIDE_FRAGMENT);

        String result = strategy.generateConfiguration(device);

        String expectedConfig = "username=user\n" +
                "password=pass\n" +
                "domain=override.com\n" +
                "port=9090\n" +
                "codecs=АА12\n" +
                "timeout=10\n";

        assertEquals(expectedConfig, result);
    }

    @Test
    @DisplayName("Generate device configuration with partial override fragment")
    void generateConfiguration_WithPartialOverrideFragment_ReturnsConfig() {
        device.setOverrideFragment(PARTIAL_OVERRIDE_FRAGMENT);

        String result = strategy.generateConfiguration(device);

        String expectedConfig = "username=user\n" +
                "password=pass\n" +
                "port=9090\n" +
                "domain=sip.voxloud.com\n" +
                "codecs=G711,G729\n";

        assertEquals(expectedConfig, result);
    }

}