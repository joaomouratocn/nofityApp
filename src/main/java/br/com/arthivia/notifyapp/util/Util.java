package br.com.arthivia.notifyapp.util;

import br.com.arthivia.notifyapp.model.NotificationDao;
import br.com.arthivia.notifyapp.model.NotificationTable;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String convertDayToString(Integer dayInt) {
        return switch (dayInt) {
            case 1 -> "SEGUNDA";
            case 2 -> "TERÇA";
            case 3 -> "QUARTA";
            case 4 -> "QUINTA";
            case 5 -> "SEXTA";
            case 6 -> "SABADO";
            case 7 -> "DOMINDO";
            default -> throw new IllegalStateException("Unexpected value: " + dayInt);
        };
    }

    public static List<Integer> convertDayList(List<String> daylist) {
        List<Integer> outlist = new ArrayList<>();
        for (String day : daylist) {
            var dayWeek = switch (day) {
                case "SEGUNDA" -> 1;
                case "TERÇA" -> 2;
                case "QUARTA" -> 3;
                case "QUINTA" -> 4;
                case "SEXTA" -> 5;
                case "SABADO" -> 6;
                case "DOMINGO" -> 7;
                default -> throw new IllegalStateException("Unexpected value: " + day);
            };
            outlist.add(dayWeek);
        }
        return outlist;
    }

    public static NotificationTable convertNotificationTable(NotificationDao notificationDao){
        List<String> dayStringList = new ArrayList<>();
        for(Integer dayWeek: notificationDao.getDayWeek()){
            dayStringList.add(convertDayToString(dayWeek));
        }
        return new NotificationTable(
            notificationDao.getId(),
            notificationDao.getTitle(),
            notificationDao.getMessage(),
            dayStringList,
            notificationDao.getHour(),
            notificationDao.getEnable(),
            notificationDao.getNotified()
        );
    }
}
