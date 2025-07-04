package br.com.arthivia.notifyapp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogApp {
    private static final String LOG_FILE = "logs.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static LogApp logApp;

    private LogApp(){}

    public static LogApp getInstance(){
        if(logApp == null){
            logApp = new LogApp();
        }
        return logApp;
    }

    public void logError(String errorMessage){
        try{
            Path path = Paths.get(LOG_FILE);
            String logEntry = String.format("[%s] Error: %s%n", LocalDateTime.now().format(DATE_TIME_FORMATTER),errorMessage);

            if(Files.exists(path)){
                Files.write(path, logEntry.getBytes(), StandardOpenOption.APPEND);
            }else{
                Files.write(path, logEntry.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            System.err.println("Falha ao registrar logs: " + e.getMessage());
        }
    }
}
