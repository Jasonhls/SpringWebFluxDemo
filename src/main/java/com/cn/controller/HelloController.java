package com.cn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @description:  技术栈从命令式的、同步阻塞的spring-webmvc + servlet + Tomcat  ---> 响应式的、异步非阻塞的spring-webflux + Reactor + Netty
 *
 * 在Java 7推出异步I/O库，以及Servlet3.1增加了对异步I/O的支持之后，Tomcat等Servlet容器也随后开始支持异步I/O，
 * 然后Spring WebMVC也增加了对Reactor库的支持，所以上边第4）步如果不是将spring-boot-starter-web替换为spring-boot-starter-WebFlux，
 * 而是增加reactor-core的依赖的话，仍然可以用注解的方式开发基于Tomcat的响应式应用。
 * @author: helisen
 * @create: 2021-01-23 09:59
 **/
@RestController
public class HelloController {
    @GetMapping(value = "/test")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world");
    }

    /*@GetMapping(value = "/test")
    public String hello() {
        return "Welcome to reactive world";
    }*/
}
