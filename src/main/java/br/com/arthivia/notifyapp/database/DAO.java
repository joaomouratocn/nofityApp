package br.com.arthivia.notifyapp.database;

import br.com.arthivia.notifyapp.model.NotificationDao;
import br.com.arthivia.notifyapp.util.LogApp;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {
    private final Connection connection;
    private static DAO dao;

    private DAO() {
        connection = DatabaseConnection.getInstance();
    }

    public static DAO getInstance(){
        if (dao == null){
            dao = new DAO();
        }
        return dao;
    }

    public boolean insertNotification(NotificationDao notificationDao) {
        String sqlInsert = "INSERT INTO notify(title, message, hour, enable, notified) VALUES (?, ?, ?, ?,?)";
        int generatedId = 0;

        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, notificationDao.getTitle());
            preparedStatement.setString(2, notificationDao.getMessage());
            preparedStatement.setString(3, notificationDao.getHour());
            preparedStatement.setInt(4, notificationDao.getEnable());
            preparedStatement.setInt(5, notificationDao.getNotified());

            int affectRows = preparedStatement.executeUpdate();

            if (affectRows > 0) {
                try {
                    var rs = preparedStatement.getGeneratedKeys();
                    {
                        if (rs.next()) {
                            generatedId = rs.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    LogApp.getInstance().logError("Erro ao tentar executa");
                    System.out.println(e.getMessage());
                }
            }

            if (generatedId != 0) {
                boolean result = insertDayOfWeek(notificationDao.getDayWeek(), generatedId);
                if (!result) {
                    connection.rollback();
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            LogApp.getInstance().logError("Erro ao tenta executar esta operação " + e.getMessage());
            return false;
        }
    }

    public void setNotified(Integer notifyId) {
        String sqlSetNotified = "UPDATE notify SET notified=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSetNotified)) {
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, notifyId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean insertDayOfWeek(List<DayOfWeek> daysWeek, int generatedId) throws SQLException {
        String sqlInsertDayWeek = "INSERT INTO notifydayweek(notifyid, dayweek)VALUES(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertDayWeek)) {
            for (DayOfWeek day : daysWeek) {
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setInt(2, day.getValue());

                preparedStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            LogApp.getInstance().logError("Erro ao tentar executar esta operação " + e.getMessage());
            return false;
        }
    }

    public String deleteNotification(Integer id) {
        String sqlDeleteNotification = "DELETE FROM notify where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteNotification)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            return "Dado deletado com sucesso!";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error ao deletar dado: " + e.getMessage();
        }
    }

    public boolean updateNotification(NotificationDao notificationDao) {
        String sqlUpdate = """
                    UPDATE notify SET title=?, message=?, "hour"=?, enable=?, notified=? WHERE id=?;
                """;


        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, notificationDao.getTitle());
            preparedStatement.setString(2, notificationDao.getMessage());
            preparedStatement.setString(3, notificationDao.getHour());
            preparedStatement.setInt(4, notificationDao.getEnable());
            preparedStatement.setInt(5, notificationDao.getNotified());
            preparedStatement.setInt(6, notificationDao.getId());

            preparedStatement.executeUpdate();

            boolean resultClearDayWeeks = clearDayWeeks(notificationDao.getId());

            if (!resultClearDayWeeks) {
                connection.rollback();
                return false;
            }

            boolean resultInsertDays = insertDayOfWeek(notificationDao.getDayWeek(), notificationDao.getId());
            if (!resultInsertDays) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            LogApp.getInstance().logError("Erro ao tentar executar esta operação: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LogApp.getInstance().logError("Erro ao tentar executar esta operção: " + e.getMessage());
            }
        }
    }

    private boolean clearDayWeeks(Integer notificationId) {
        String sqlCleanDaysWeek = """
                    DELETE FROM notifydayweek WHERE notifyid=?;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCleanDaysWeek)) {
            preparedStatement.setInt(1, notificationId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            LogApp.getInstance().logError("Erro ao tentar executar esta tarefa: " + e.getMessage());
            return false;
        }
    }

    public List<NotificationDao> getAllNotification() {
        String sqlGetAll = """
                    SELECT n.*, ndw.dayweek FROM notify n
                    LEFT JOIN notifydayweek ndw ON n.id = ndw.notifyid
                    ORDER BY n.id
                """;

        List<NotificationDao> list;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlGetAll);

            Map<Integer, NotificationDao> map = new HashMap<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                NotificationDao notificationDao = map.get(id);

                if (notificationDao == null) {
                    notificationDao = new NotificationDao(
                            id,
                            resultSet.getString("title"),
                            resultSet.getString("message"),
                            new ArrayList<>(),
                            resultSet.getString("hour"),
                            resultSet.getInt("enable"),
                            resultSet.getInt("notified")
                    );
                    map.put(id, notificationDao);
                }
                int dayValue = resultSet.getInt("dayweek");
                if (dayValue >= 1 && dayValue <= 7) {
                    notificationDao.getDayWeek().add(DayOfWeek.of(dayValue));
                }
            }
            list = new ArrayList<>(map.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
