package com.example.camel_sql.route;

import com.example.camel_sql.entity.RouteConfig;
import com.example.camel_sql.repository.FileStorageRepository;
import com.example.camel_sql.repository.RouteConfigRepository;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.camel_sql.utility.Constants.OUTGOING;

@Component
public class OutgoingRouter extends RouteBuilder {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private RouteConfigRepository routeConfigRepository;

    @Override
    public void configure() throws Exception {
        try {
            List<RouteConfig> routeConfigs = routeConfigRepository.findAll();

            List<RouteConfig> outgoingRouteConfigs = routeConfigs.stream()
                    .filter(routeConfig -> OUTGOING.equals(routeConfig.getSourceEndpointType()))
                    .collect(Collectors.toList());



        }
        catch (Exception e){

        }
    }
}
