package com.voxloud.provisioning.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.voxloud.provisioning.service.ProvisioningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ProvisioningControllerTest {
    public static final String VALID_MAC_ADDRESS = "1a-2b-3c-4d-5e-6f";
    public static final String INVALID_MAC_ADDRESS = "1a-2b-3c-4d-5e-00";
    public static final String EXPECTED_CONFIGURATION = "Configuration data";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProvisioningService provisioningService;

    @InjectMocks
    private ProvisioningController provisioningController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(provisioningController).build();
    }

    @Test
    @DisplayName("Get device configuration by valid MAC address")
    void getDeviceConfiguration_ValidMacAddress_ReturnsConfiguration() throws Exception {


        given(provisioningService.getProvisioningFile(VALID_MAC_ADDRESS)).willReturn(EXPECTED_CONFIGURATION);

        mockMvc.perform(get("/api/v1/provisioning/{VALID_MAC_ADDRESS}", VALID_MAC_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_CONFIGURATION));
    }

    @Test
    @DisplayName("Get device configuration by invalid MAC address")
    void getDeviceConfiguration_InvalidMacAddress_ReturnsBadRequest() throws Exception {
        String requestJson = String.format("{\"macAddress\":\"%s\"}", INVALID_MAC_ADDRESS);

        mockMvc.perform(post("/api/device/configuration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }
}
