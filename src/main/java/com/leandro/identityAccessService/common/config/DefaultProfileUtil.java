package com.leandro.identityAccessService.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class DefaultProfileUtil {
    private static final String SPRING_PROFILE_ACTIVE = "spring.profiles.active";
    private static final Properties BUILD_PROPERTIES = readProperties();

    public static String getDefaultActiveProfiles() {
        if (BUILD_PROPERTIES != null) {
            String activeProfile = BUILD_PROPERTIES.getProperty(SPRING_PROFILE_ACTIVE);
            if (activeProfile != null && !activeProfile.isEmpty() &&
                    (activeProfile.contains(Profiles.SPRING_PROFILE_DEVELOPMENT)
                            || activeProfile.contains(Profiles.SPRING_PROFILE_PRODUCTION)
                            || activeProfile.contains(Profiles.SPRING_PROFILE_LOCAL))) {
                return activeProfile;
            }
        }
        log.warn("No Spring profile configured, running with default profile: {}", Profiles.SPRING_PROFILE_LOCAL);
        return Profiles.SPRING_PROFILE_LOCAL;
    }

    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put(SPRING_PROFILE_ACTIVE, getDefaultActiveProfiles());
        app.setDefaultProperties(defProperties);
    }

    private static Properties readProperties() {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(new ClassPathResource("config/application.yml"));
            return factory.getObject();
        } catch (Exception e) {
            log.error("Failed to read application.yml to get default profile");
        }
        return null;
    }
}

