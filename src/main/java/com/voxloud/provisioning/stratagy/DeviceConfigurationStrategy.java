package com.voxloud.provisioning.stratagy;

import com.voxloud.provisioning.entity.Device;

public interface DeviceConfigurationStrategy {
    String generateConfiguration(Device device);
}
