package br.com.arthivia.notifyapp.util;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.DayOfWeek.*;

public class Util {

    public static List<DayOfWeek> convertDayWeek(List<String> dayWeek) {
        var outputList = new ArrayList<DayOfWeek>();
        for (String day : dayWeek) {
            var value = switch (day) {
                case "Seg." -> MONDAY;
                case "Ter." -> TUESDAY;
                case "Qua." -> WEDNESDAY;
                case "Qui." -> THURSDAY;
                case "Sex." -> FRIDAY;
                case "Sab." -> SATURDAY;
                case "Dom." -> SUNDAY;
                default -> throw new IllegalStateException("Unexpected value: " + day);
            };
            outputList.add(value);
        }
        return outputList;
    }

    public static void startNotificationService() {
        var dao = DAO.getInstance();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                LocalDateTime now = LocalDateTime.now();
                DayOfWeek today = now.getDayOfWeek();
                String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));

                List<NotificationDao> notifications = dao.getAllNotification();

                for (NotificationDao notificationDao : notifications) {
                    if (!notificationDao.getDayWeek().contains(today)) continue;
                    if (!notificationDao.getHour().equals(currentTime)) continue;

                    if (notificationDao.getNotified() == 0) {
                        System.out.println(notificationDao.getTitle());
                        dao.setNotified(notificationDao.getId());
                    }
                }

            } catch (Exception e) {
                LogApp.getInstance().logError("Erro ao tentar executar este operção: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}
