package com.howtodoinjava.demo.config;

import java.util.List;

import com.howtodoinjava.demo.model.Employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class EmployeeItemWriter implements ItemWriter<Employee> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeItemWriter.class);

    // private Person person;

    @Override
    public void write(List<? extends Employee> items) throws Exception {
        if (!items.isEmpty()) {
            for (final Employee employee : items) {
                System.out.println("writing item..: " + employee);
                LOGGER.debug("writing item..: " + employee);
            }
        }

    }

}
