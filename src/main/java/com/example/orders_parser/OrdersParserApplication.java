package com.example.orders_parser;

import com.example.orders_parser.threads.ParsingThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Semaphore;

@SpringBootApplication
public class OrdersParserApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(OrdersParserApplication.class, args);
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
        Semaphore semaphore = (Semaphore) applicationContext.getBean("semaphore");

        for (String fileName : args) {
            ParsingThread parsingThread = (ParsingThread) applicationContext.getBean("parsingThread");
            parsingThread.setName(fileName);

            while (semaphore.availablePermits() <= 1) {
            }

            semaphore.acquireUninterruptibly();

            taskExecutor.execute(parsingThread);
        }

        while (true) {
            if (semaphore.availablePermits() == (int) applicationContext.getBean("threadsNumber")) {
                taskExecutor.shutdown();

                return;
            }
        }
    }
}
