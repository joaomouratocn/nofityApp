package br.com.arthivia.notifyapp.database;

import br.com.arthivia.notifyapp.util.LogApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:notifyapp.db";
    private static Connection connection;

    public DatabaseConnection() {}

    public static Connection getInstance() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                verifyTables(connection);
            } catch (SQLException e) {LogApp.logError(e.getMessage());
                System.out.println("Erro ao tentar acessar o banco de dados: " + e.getMessage());
            }
        }
        return connection;
    }

    private static void verifyTables(Connection connection) {
        String tableNotify = """
            CREATE TABLE IF NOT EXISTS notify(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                message TEXT,
                hour TEXT NOT NULL,
                enable INTEGER NOT NULL,
                notified INTEGER NOT NULL
            );
        """;

        String tableDayWeekNotify = """
            CREATE TABLE IF NOT EXISTS notifydayweek (
                notifyid INTEGER NOT NULL,
                dayweek INTEGER NOT NULL,
                PRIMARY KEY (notifyid, dayweek),
                FOREIGN KEY (notifyid) REFERENCES notify(id) ON DELETE CASCADE
            );
        """;


        try (Statement statement = connection.createStatement()) {
            statement.execute(tableNotify);
            System.out.println("tabela de notificação criada");
            statement.execute(tableDayWeekNotify);
            System.out.println("tabela dias das notificações criada");
        } catch (SQLException e) {
            LogApp.logError("Error ao realizar esta operação: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
