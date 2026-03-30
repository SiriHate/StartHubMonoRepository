package org.siri_hate.api_gateway.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "custom-gateway-properties")
public class CustomGatewayProperties {

    private List<String> filterExcludePaths;

    public List<String> getFilterExcludePaths() {
        return filterExcludePaths;
    }

    public void setFilterExcludePaths(List<String> filterExcludePaths) {
        this.filterExcludePaths = filterExcludePaths;
    }
}
