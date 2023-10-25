package com.example.crm.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @SpringBootApplication  Need to check uses of this application.
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryServer {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryServer.class, args);
    }
}
