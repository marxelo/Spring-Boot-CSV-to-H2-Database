package com.howtodoinjava.demo.config;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.howtodoinjava.demo.model.Employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomSkippedListener {

    @OnSkipInRead
    public void onSkipInRead(Throwable throwable) {
        if (throwable instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) throwable;
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Ocorreu um erro ao ler a linha " + ffpe.getLineNumber()
                    + "  do arquivo. Abaixo está o registro com problema.\n");
            errorMessage.append(">>" + ffpe.getInput() + "<<\n");
            LOGGER.error("{}", errorMessage.toString());
            // System.out.println(ffpe.getLocalizedMessage());

        } 
    }

    @OnSkipInWrite
    public void onSkipInWrite(Employee item, Throwable throwable) {
        LOGGER.info("Erro ao gravar o arquivo: " + throwable.getMessage());
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Exception: " + throwable.getClass() + "\n");
                // + " line of the file. Below was the faulty " + "input.\n");
        errorMessage.append("Mensagem adicional1: " + throwable.getLocalizedMessage() + "\n");
        errorMessage.append("Mensagem adicional2: " + throwable.getCause() + "\n");
        errorMessage.append("Employee: " + item.toString());
        LOGGER.error("{}", errorMessage.toString());
        // LOGGER.info("balabla" + throwable.getMessage());
    }

    @OnSkipInProcess
    public void onSkipInProcess(Employee item, Throwable throwable) {
        if (throwable instanceof IllegalArgumentException) {
            IllegalArgumentException ffpe = (IllegalArgumentException) throwable;
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Argumento invalido. Exception: " + ffpe.getClass() + "\n");
                    // + " line of the file. Below was the faulty " + "input.\n");
            errorMessage.append("Mensagem adicional1: " + ffpe.getLocalizedMessage() + "\n");
            errorMessage.append("Mensagem adicional2: " + ffpe.getCause() + "\n");
            errorMessage.append("Employee: " + item.toString());
            LOGGER.error("{}", errorMessage.toString());
            // System.out.println(ffpe.getLocalizedMessage());

        } else {
            LOGGER.info("Demais erros no processor: " + throwable.getMessage() + throwable.getStackTrace());
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Exception: " + throwable.getClass() + "\n");
                    // + " line of the file. Below was the faulty " + "input.\n");
            errorMessage.append("Mensagem adicional1: " + throwable.getLocalizedMessage() + "\n");
            errorMessage.append("Mensagem adicional2: " + throwable.getCause() + "\n");
            errorMessage.append("Employee: " + item.toString());
            LOGGER.error("{}", errorMessage.toString());
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSkippedListener.class);
}