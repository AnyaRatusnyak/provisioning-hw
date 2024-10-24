package com.voxloud.provisioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.voxloud.provisioning.entity")
@EnableJpaRepositories(basePackages = "com.voxloud.provisioning.repository")
public class ProvisioningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProvisioningApplication.class, args);
    }

}