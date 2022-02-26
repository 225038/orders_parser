
package com.example.orders_parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Semaphore;

@Configuration
public class ThreadPoolConfig {
    private final int threadsNumber = 4;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();

        pool.setCorePoolSize(threadsNumber);
        pool.setWaitForTasksToCompleteOnShutdown(true);

        return pool;
    }

    @Bean
    public Semaphore semaphore() {
        return new Semaphore(threadsNumber);
    }

    @Bean
    public int threadsNumber() {
        return threadsNumber;
    }
}

