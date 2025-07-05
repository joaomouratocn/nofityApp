package br.com.arthivia.notifyapp.service;

import br.com.arthivia.notifyapp.database.DAO;
import br.com.arthivia.notifyapp.model.NotificationDao;
import br.com.arthivia.notifyapp.util.LogApp;
import br.com.arthivia.notifyapp.util.Util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService {
    private final ScheduledExecutorService scheduler;
    private final DAO dao = DAO.getInstance();

    public NotificationService() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startNotificationService() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Monitorando notificações");
            try {
                LocalDateTime now = LocalDateTime.now();
                DayOfWeek today = now.getDayOfWeek();
                String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));

                List<NotificationDao> notifications = dao.getAllNotification();

                for (NotificationDao notificationDao : notifications) {
                    if (!notificationDao.getDayWeek().contains(today)) continue;
                    if (!notificationDao.getHour().equals(currentTime)) continue;

                    if (notificationDao.getNotified() == 0) {
                        System.out.println("disparou notificação");
                        Util.openNotificationScreen(notificationDao);
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                LogApp.getInstance().logError("Erro ao tentar executar este operção: " + e.getMessage());
            }
        },0, 1, TimeUnit.MINUTES);
    }
}
