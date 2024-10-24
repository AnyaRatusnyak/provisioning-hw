package com.voxloud.provisioning.service;

import com.voxloud.provisioning.annotation.DeviceModelSupported;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.repository.DeviceRepository;
import com.voxloud.provisioning.stratagy.DeviceConfigurationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {
    private final Map<Device.DeviceModel, DeviceConfigurationStrategy> strategies;
    private final DeviceRepository deviceRepository;

    @Autowired
    public ProvisioningServiceImpl(List<DeviceConfigurationStrategy> strategyList, DeviceRepository deviceRepository) {
        this.strategies = strategyList.stream().collect(Collectors.toMap(
                strategy -> strategy.getClass().getAnnotation(DeviceModelSupported.class).value(),
                strategy -> strategy
        ));
        this.deviceRepository = deviceRepository;
    }


    @Override
    public String getProvisioningFile(String macAddress) {
        Device device = deviceRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new DeviceNotFoundException("Device with MAC address " + macAddress + " not found"));

        DeviceConfigurationStrategy strategy = strategies.get(device.getModel());

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported device model: " + device.getModel());
        }

        return strategy.generateConfiguration(device);
    }

}
