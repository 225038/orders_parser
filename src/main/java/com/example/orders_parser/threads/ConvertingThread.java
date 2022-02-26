
package com.example.orders_parser.threads;

import com.example.orders_parser.dto.Order;
import com.example.orders_parser.service.ResultPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;

@Component
@Scope("prototype")
public final class ConvertingThread implements Runnable {
    @Autowired
    private Semaphore semaphore;

    String fileName;
    Integer lineNumber;
    Order order;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        try {
            ResultPrinterService.printOrderResult(order, fileName, lineNumber);
        } finally {
            semaphore.release();
        }
    }
}
