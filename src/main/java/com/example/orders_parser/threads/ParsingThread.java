
package com.example.orders_parser.threads;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.service.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

@Component
@Scope("prototype")
public final class ParsingThread implements Runnable {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Semaphore semaphore;

    @Autowired
    FileParser fileParser;
    String fileName;

    public void setName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            HashMap<Integer, Order> orders = fileParser.handleFileParsing(fileName);

            if (orders != null && !orders.isEmpty()) {
                orders.forEach((index, order) -> {
                    ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");

                    ConvertingThread convertingThread = (ConvertingThread) applicationContext.getBean("convertingThread");
                    convertingThread.setFileName(fileName);
                    convertingThread.setLineNumber(index);
                    convertingThread.setOrder(order);

                    semaphore.acquireUninterruptibly();
                    taskExecutor.execute(convertingThread);
                });
            }
        } finally {
            semaphore.release();
        }
    }
}
