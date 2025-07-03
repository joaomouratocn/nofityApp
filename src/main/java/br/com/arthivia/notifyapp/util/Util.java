package br.com.arthivia.notifyapp.util;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static java.time.DayOfWeek.*;

public class Util {

    public static List<DayOfWeek> convertDayWeek(List<String> dayWeek) {
        var outputList = new ArrayList<DayOfWeek>();
        for (String day : dayWeek) {
            outputList.add(switch (day) {
                case "Seg." -> MONDAY;
                case "Ter." -> TUESDAY;
                case "Qua." -> WEDNESDAY;
                case "Qui." -> THURSDAY;
                case "Sex." -> FRIDAY;
                case "Sab." -> SATURDAY;
                case "Dom." -> SUNDAY;
                default -> throw new IllegalStateException("Unexpected value: " + day);
            });
        }
        return outputList;
    }
}
