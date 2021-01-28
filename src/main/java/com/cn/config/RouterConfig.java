package com.cn.config;

import com.cn.component.TimeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @description:
 * @author: helisen
 * @create: 2021-01-23 10:45
 **/
@Configuration
public class RouterConfig {
    @Autowired
    private TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timeRouter() {
        return route(GET("/time"), req -> timeHandler.getTime(req))
                //这种方式相比于上一行更简洁
                .andRoute(GET("/date"), timeHandler::getDate)
                .andRoute(GET("/times"), timeHandler::sendTimePerSec);
    }

    @Bean
    public RouterFunction<ServerResponse> timerRouter() {
        return route(GET("/baidu"), req -> timeHandler.queryAddress(req));
    }

    @Bean
    public RouterFunction<ServerResponse> timeWithFallbackRouter() {
        return route(GET("/qq"), req -> timeHandler.queryAddressWithFallback(req));
    }

    @Bean
    public RouterFunction<ServerResponse> timeWithException() {
        return route(GET("/weixin"), req -> timeHandler.queryAddressWithException(req));
    }

    @Bean
    public RouterFunction<ServerResponse> timeWithErrorReturn() {
        return route(GET("/other"), req -> timeHandler.queryAddressWithErrorReturn(req));
    }
}
