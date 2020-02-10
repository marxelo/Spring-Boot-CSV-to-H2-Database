package com.howtodoinjava.demo.config;
 
import org.springframework.batch.item.ItemProcessor;

import com.howtodoinjava.demo.model.Employee;
 
public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee>
{
    public Employee process(Employee employee) throws Exception
    {
        int i = Integer.parseInt(employee.getId());
        int e = 10/i;
        System.out.println("Inserting employee : " + employee);
        return employee;
    }
}