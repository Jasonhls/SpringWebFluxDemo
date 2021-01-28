package com.cn.test;

import com.cn.exception.NameRequiredException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.cn.component.TimeHandler.queryAddressByName;


/**
 * @description:
 * @author: helisen
 * @create: 2021-01-26 16:22
 **/
public class TestDemo {
    private Logger log = LoggerFactory.getLogger(TestDemo.class);

    @Test
    public void onErrorReturn () {
        Flux.interval(Duration.ofMillis(10))
                .map(i -> {
                    System.out.println("数字为：" + i);
                    if (i == 10) {
                        System.out.println("当前数字为：" + i);
                        throw new RuntimeException("fake a mistake");
                    }
                    return String.valueOf("结果为：" + i);
                })
                .doOnError(e -> log.error("error 类型：{}， error 消息： {}", e.getClass(),e.getMessage()))
                // 遇到error直接返回指定value， 错误类型判断可选
                .onErrorReturn("test on error return")
                .subscribe(log::info);

        try{
            Thread.sleep(2000);
            System.out.println("结束了。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        queryAddressByName("weixin")
                .flatMap(e -> Mono.just(queryAddressByName("other").toString()))
                .onErrorReturn("there is a error")
                .subscribe(e -> System.out.println(e));
    }

    @Test
    public void test2() {
//        Mono.just("there is a error").subscribe(e -> System.out.println(e));
        queryAddressByName("weixin")
                .flatMap(e -> Mono.just(queryAddressByName("other").toString()))
                .onErrorResume(e -> Mono.error(
                            new NameRequiredException(HttpStatus.BAD_REQUEST, "username is required", e)
                        )
                )
                .subscribe(e -> System.out.println(e.toString()));
    }




    @Test
    public void onErrorResume () {
        Flux.interval(Duration.ofMillis(100))
                .map(i -> {
                    if (i == 2) {
//                        throw new RuntimeException("fake a mistake");  // 设置两个错误，一个runtime错误，一个zero错误
                        throw new NullPointerException("fake a mistake");  // 设置两个错误，一个runtime错误，一个zero错误
                    }
                    return String.valueOf(100/(i-5));
                })
                .doOnError(e -> {
                    log.error("error 类型：{}， error 消息： {}", e.getClass(),e.getMessage());
                    if(e instanceof NullPointerException) {
                        System.out.println("哈哈哈哈哈");
                    }
                })
                //一旦遇到error可以用来返回备选方案， 错误类型判断可选
                .onErrorResume(e -> Flux.range(1,3).map(String::valueOf))
                .subscribe(log::info);

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void onErrorContinue () {
        Flux.interval(Duration.ofMillis(100))
                .map(i -> {
                    if (i == 2) {
                        throw new RuntimeException("fake a mistake");
                    }
                    return String.valueOf(100/(i-5));
                })
                // 遇到error之后跳过，可以通过不同错误类型做不同处理
                .onErrorContinue((err, val) -> log
                        .error("处理第{}个元素时遇到错误，错误类型为：{}， 错误信息为： {}", val, err.getClass(), err.getMessage()))
                .subscribe(log::info);

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void onErrorMap () {
        Flux.interval(Duration.ofMillis(100))
                .map(i -> {
                    if (i == 2) {
                        throw new RuntimeException("fake a mistake");
                    }
                    return String.valueOf(100/(i-5));
                })
                // 当发生错误时更换错误内容
                .onErrorMap(e -> new RuntimeException("change error type"))
                .subscribe(log::info);

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
