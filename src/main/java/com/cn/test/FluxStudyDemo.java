package com.cn.test;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: helisen
 * @create: 2021-01-26 17:28
 **/
public class FluxStudyDemo {
    @Test
    public void test1() {
        Flux.error(new Exception("a wo,something is wrong!")).subscribe(System.out::println);
    }

    @Test
    public void fluxJustTest() {
        Flux.just("1", "A", 3).subscribe(System.out::println);
    }

    /**
     * 顾名思义就是每隔一定时间，发射一个数据（从0开始），上面的示例表示每隔500毫秒，从0开始递增，发射1个数字
     * @throws InterruptedException
     */
    @Test
    public void fluxIntervalTest() throws InterruptedException {
        Flux.interval(Duration.of(500, ChronoUnit.MILLIS)).subscribe(System.out::println);
        //防止程序过早退出，放一个CountDownLatch拦住
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    @Test
    public void fluxEmptyTest() {
        Flux.empty().subscribe(System.out::println);
    }

    /**
     * generate中next只能调1次，否则会报错 reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalStateException: More than one call to onNext
     */
    @Test
    public void fluxGenerateTest() {
        Flux.generate(i -> {
            i.next("AAAAA");
            //i.next("BBBBB");//注意generate中next只能调用1次
            i.complete();
        }).subscribe(System.out::println);

        final Random rnd = new Random();
        Flux.generate(ArrayList::new, (list, item) -> {
            Integer value = rnd.nextInt(100);
            list.add(value);
            item.next(value);
            if (list.size() >= 10) {
                item.complete();
            }
            return list;
        }).subscribe(System.out::println);
    }

    /**
     * create方法则没有next的调用次数限制
     */
    @Test
    public void fluxCreateTest() {
        Flux.create(i -> {
            i.next("A");
            i.next("B");
            i.complete();
        }).subscribe(System.out::println);

        final Random rnd = new Random();
        Flux.create(item -> {
            for (int i = 0; i < 10; i++) {
                item.next(i);
            }
        }).subscribe(System.out::println);
    }

    @Test
    public void fluxBufferTest() throws InterruptedException {
        Flux.range(0, 10).buffer(3).subscribe(System.out::println);

        System.out.println("--------------");

        Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .bufferTimeout(2, Duration.of(2, ChronoUnit.SECONDS))
                .subscribe(System.out::println);

        //防止程序过早退出，放一个CountDownLatch拦住
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    @Test
    public void fluxFilterTest() {
        Flux.range(0, 10).filter(c -> c % 2 == 0).subscribe(System.out::println);
    }

    /**
     * 就是把各组元素，按位组合（就算用拉链袋封起来一样，因此得名），注意：这里有一个木桶原则，即 元素最少的"组"，决定了最后输出的"组"个数。
     */
    @Test
    public void fluxZipTest() {
        Flux.just("A", "B", "C", "D").zipWith(Flux.just("1", "2", "3")).subscribe(System.out::println);
    }

    /**
     * take与takeLast很好理解，就是前n个或后n个。　takeWhile与takeUntil 需要记忆一下：
     *
     * takeWhile 是先判断条件是否成立，然后再决定是否取元素（换言之，如果一开始条件不成立，就直接终止了）；
     *
     * takeUntil 是先取元素，直到遇到条件成立，才停下
     *
     * takeUntilOther 则是先取元素，直到别一个Flux序列产生元素
     */
    @Test
    public void fluxTakeTest() {
//        Flux.range(1, 10).take(3).subscribe(System.out::println);
//        System.out.println("--------------");
//        Flux.range(1, 10).takeLast(3).subscribe(System.out::println);
//        System.out.println("--------------");
//        Flux.range(1, 10).takeWhile(c -> c >= 1 && c < 5).subscribe(System.out::println);
//        System.out.println("--------------");
//        Flux.range(1, 10).takeUntil(c -> c > 2 && c < 5).subscribe(System.out::println);
//        System.out.println("--------------");
        Flux.range(1, 4).takeUntilOther(Flux.never()).subscribe(System.out::println);
    }
}
