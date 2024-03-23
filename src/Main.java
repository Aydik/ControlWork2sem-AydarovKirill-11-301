import pack.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Считать файл в список строк.
        List<String> lines = Files.readAllLines(new File("src\\schedule.txt").toPath(), Charset.defaultCharset());

        // 4. Создать и наполнить данными из файла структуру Map<время, List<программа>>
        Map<BroadcastsTime, List<Program>> programsMap = new HashMap<>();
        // 5. Создать List<программа> со всеми программами всех каналов
        List<Program> allPrograms = new ArrayList<>();
        String currentChannel = "";

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("#")) {
                currentChannel = lines.get(i).substring(1).trim();
                continue;
            }
            if (Character.isDigit(lines.get(i).charAt(0))) {
                byte hour = Byte.parseByte(lines.get(i).split(":")[0]);
                byte minutes = Byte.parseByte(lines.get(i).split(":")[1]);
                BroadcastsTime time = new BroadcastsTime(hour, minutes);
                String title = lines.get(i + 1);
                Program program = new Program(currentChannel, time, title);
                programsMap.computeIfAbsent(time, k -> new ArrayList<>()).add(program);
                allPrograms.add(program);
            }
        }

        // 6. вывести все программы в порядке возрастания канал, время показа
        Collections.sort(allPrograms, new MyComparator());
        for (Program i : allPrograms)
            System.out.println(i);

        // 7. вывести все программы, которые идут сейчас
        BroadcastsTime timeNow = new BroadcastsTime(LocalTime.now());
        System.out.println();
        for (int i = 0; i < allPrograms.size() - 1; i++) {
            if (timeNow.between(allPrograms.get(i).getTime(), allPrograms.get(i + 1).getTime())) {
                System.out.println(allPrograms.get(i));
            } else if (timeNow.compareTo(allPrograms.get(i).getTime()) > 0 &&
                    !(allPrograms.get(i).getChannel().equals(allPrograms.get(i + 1).getChannel()))) {
                System.out.println(allPrograms.get(i));
            }
        }
        if (timeNow.compareTo(allPrograms.get(allPrograms.size() - 1).getTime()) > 0) { // проверка последней програмы
            System.out.println(allPrograms.get(allPrograms.size() - 1));
        }

        // 8. найти все программы по некоторому названию
        System.out.println();
        String nazvanie = "Новости (с субтитрами)";
        for (Program i : allPrograms)
            if (i.getTitle().equals(nazvanie))
                System.out.println(i);

        // 9. найти все программы определенного канала, которые идут сейчас
        System.out.println();
        String channel = "Россия 1";
        for (int i = 0; i < allPrograms.size() - 1; i++) {
            if (timeNow.between(allPrograms.get(i).getTime(), allPrograms.get(i + 1).getTime()) &&
                    allPrograms.get(i).getChannel().equals(channel)) {
                System.out.println(allPrograms.get(i));
            } else if (timeNow.compareTo(allPrograms.get(i).getTime()) > 0 &&
                    allPrograms.get(i).getChannel().equals(channel) &&
                    !(allPrograms.get(i + 1).getChannel().equals(channel))) {
                System.out.println(allPrograms.get(i));
            }
        }
        if (timeNow.compareTo(allPrograms.get(allPrograms.size() - 1).getTime()) > 0 &&
                allPrograms.get(allPrograms.size() - 1).getChannel().equals(channel)) { // проверка последней програмы
            System.out.println(allPrograms.get(allPrograms.size() - 1));
        }

        //10. найти все программы определенного канала, которые будут идти в некотором промежутке времени
        System.out.println();
        BroadcastsTime time1 = new BroadcastsTime((byte) 23, (byte) 40);
        for (int i = 0; i < allPrograms.size() - 1; i++) {
            if (time1.between(allPrograms.get(i).getTime(), allPrograms.get(i + 1).getTime()) &&
                    allPrograms.get(i).getChannel().equals(channel)) {
                System.out.println(allPrograms.get(i));
            } else if (time1.compareTo(allPrograms.get(i).getTime()) > 0 &&
                    allPrograms.get(i).getChannel().equals(channel) &&
                    !(allPrograms.get(i + 1).getChannel().equals(channel))) {
                System.out.println(allPrograms.get(i));
            }
        }
        if (time1.compareTo(allPrograms.get(allPrograms.size() - 1).getTime()) > 0 &&
                allPrograms.get(allPrograms.size() - 1).getChannel().equals(channel)) { // проверка последней програмы
            System.out.println(allPrograms.get(allPrograms.size() - 1));
        }
    }

    private static class MyComparator implements Comparator<Program> {
        @Override
        public int compare(Program program1, Program program2) {
            if (program1.getChannel().compareTo(program2.getChannel()) != 0)
                return program1.getChannel().compareTo(program2.getChannel());
            return program1.getTime().compareTo(program2.getTime());
        }
    }
}