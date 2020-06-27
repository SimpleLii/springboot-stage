package com.simplelii.app.api.utils;

import java.util.concurrent.*;

/**
 * @author SimpleLii
 * @description 线程池配置
 * @date 15:55 2020/6/22
 */
public class ThreadExectorUtil {

    private static Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static ThreadPoolExecutor executor;

    private static final Long MAX_ALIVE_TIME = 10L;
    private static final Integer MAX_IMUM_POOL_SIZE;
    private static final Integer MAX_WAIT_DEQUE_SIZE = 1000;

    static {
        MAX_IMUM_POOL_SIZE = CORE_POOL_SIZE > 20 ? CORE_POOL_SIZE : 20;
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE,
                MAX_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(MAX_WAIT_DEQUE_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ThreadExectorUtil() {
    }

    private static <V> Future<V> submit(Callable<V> task) {
        return executor.submit(task);
    }

    public static void executor(Runnable runnable) {
        executor.execute(runnable);
    }


}
