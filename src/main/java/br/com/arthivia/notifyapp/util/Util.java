//package br.com.arthivia.notifyapp.util;
//
//import br.com.arthivia.notifyapp.model.NotificationDao;
//import br.com.arthivia.notifyapp.model.NotificationTable;
//
//import java.time.DayOfWeek;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Util {
//    public static String convertDayToString(Integer dayInt) {
//        return switch (dayInt) {
//            case 1 -> "SEGUNDA";
//            case 2 -> "TERÇA";
//            case 3 -> "QUARTA";
//            case 4 -> "QUINTA";
//            case 5 -> "SEXTA";
//            case 6 -> "SABADO";
//            case 7 -> "DOMINDO";
//            default -> throw new IllegalStateException("Unexpected value: " + dayInt);
//        };
//    }
//
//    private static Integer convertIntDay(String day) {
//        return switch (day) {
//            case "SEGUNDA" -> 1;
//            case "TERÇA" -> 2;
//            case "QUARTA" -> 3;
//            case "QUINTA" -> 4;
//            case "SEXTA" -> 5;
//            case "SABADO" -> 6;
//            case "DOMINGO" -> 7;
//            default -> throw new IllegalStateException("Unexpected value: " + day);
//        };
//    }
//
//    public static NotificationTable convertNotificationTable(NotificationDao notificationDao) {
//        List<String> dayStringList = new ArrayList<>();
//        for (DayOfWeek dayWeek : notificationDao.getDayWeek()) {
//            dayStringList.add(convertDayToString(dayWeek));
//        }
//        return new NotificationTable(
//                notificationDao.getId(),
//                notificationDao.getTitle(),
//                notificationDao.getMessage(),
//                dayStringList,
//                notificationDao.getHour(),
//                notificationDao.getEnable(),
//                notificationDao.getNotified()
//        );
//    }
//
//    public static NotificationDao convertNotificationDao(NotificationTable notificationTable) {
//        List<Integer> dayIntegerList = new ArrayList<>();
//        for (String dayWeek : notificationTable.getDayWeek()) {
//            dayIntegerList.add(convertIntDay(dayWeek));
//        }
//
//        return new NotificationDao(notificationTable.getId(),
//                notificationTable.getTitle(),
//                notificationTable.getMessage(),
//                dayIntegerList,
//                notificationTable.getHour(),
//                notificationTable.getEnable(),
//                notificationTable.getNotified());
//    }
//}
