package com.cn.component;

import com.cn.exception.NameRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @description:
 * @author: helisen
 * @create: 2021-01-23 10:39
 **/
@Component
public class TimeHandler {
    private static final String TYPE_OTHER = "other";

    public Mono<ServerResponse> getTime(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }

    public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just("Today is " + new SimpleDateFormat("yyyy:MM:dd").format(new Date())), String.class);
    }

    /**
     * 1、MediaType.TEXT_EVENT_STREAM表示Content-Type为text/event-stream，即SSE；
     * 2、利用interval生成每秒一个数据的流。
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
                Flux.interval(Duration.ofSeconds(1))
                        .map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date())),
                String.class
        );
    }


    /**
     * 使用onErrorResume处理错误，fallback方法定义如下：
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> queryAddress(ServerRequest serverRequest) {
        String param = serverRequest.queryParam("type").orElse(TYPE_OTHER);
        return queryAddressByName(param)
                .flatMap(s -> ok().contentType(MediaType.TEXT_PLAIN).syncBody(s))
                .onErrorResume(e -> Mono.error(e));
    }


    /**
     * 自定义的fallback，如果请求失败就走fallback的逻辑
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> queryAddressWithFallback(ServerRequest serverRequest) {
        String param = serverRequest.queryParam("type").orElse(TYPE_OTHER);
        return queryAddressByName(param)
                .flatMap(s -> ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .syncBody(s)
                )
                .onErrorResume(e -> getTimeByTypeFallback()
                        .flatMap(s -> ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .syncBody(s))
                );
    }

    public static Mono<String> getTimeByTypeFallback() {
        return Mono.just("request is failed");
    }

    /**
     * 自定义业务异常
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> queryAddressWithException(ServerRequest serverRequest) {
        String param = serverRequest.queryParam("type").orElse(TYPE_OTHER);
        return ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(queryAddressByName(param)
                .onErrorResume(e -> Mono.error(new NameRequiredException(
                        HttpStatus.BAD_REQUEST, "username is required", e
                ))), String.class
        );
    }

    /**
     * 每当发生错误时，我们可以使用onErrorReturn()返回静态默认值
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> queryAddressWithErrorReturn(ServerRequest serverRequest){
        String param = serverRequest.queryParam("type").orElse(TYPE_OTHER);
        return queryAddressByName(param)
                .onErrorReturn("there is a error, please handle it")
                .flatMap(s -> ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .syncBody(s)
                );
    }

    public static String queryAddressByNameB(String param) {
        String type = Optional.ofNullable(param).orElse("other");
        switch (type) {
            case "baidu":
                return "www.baidu.com";
            case "qq":
                return "www.qq.com";
            case "weixin":
                return "www.weixin.com";
            case "other":
                throw new NullPointerException("throw a NullPointerException, please handle it");
            default:
                return null;
        }
    }

    public static Mono<String> queryAddressByName(String param) {
        String type = Optional.ofNullable(param).orElse("other");
        switch (type) {
            case "baidu":
                return Mono.just("www.baidu.com");
            case "qq":
                return Mono.just("www.qq.com");
            case "weixin":
                return Mono.just("www.weixin.com");
            case "other":
                throw new NullPointerException("throw a NullPointerException, please handle it");
            default:
                return Mono.empty();
        }
    }
}
