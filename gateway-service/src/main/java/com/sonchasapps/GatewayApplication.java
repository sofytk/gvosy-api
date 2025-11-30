package com.sonchasapps;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("=== GATEWAY SERVICE STARTED ===");
    }

    @Bean
    public ApplicationRunner debugFilters(GatewayProperties properties) {
        return args -> {
            System.out.println("=== REGISTERED ROUTES ===");
            properties.getRoutes().forEach(route -> {
                System.out.println(route.getId() + " -> " + route.getFilters());
            });
        };
    }
}


