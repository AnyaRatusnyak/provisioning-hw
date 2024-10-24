package com.voxloud.provisioning.annotation;

import com.voxloud.provisioning.entity.Device;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeviceModelSupported {
    Device.DeviceModel value();
}
