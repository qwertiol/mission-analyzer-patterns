package com.mycompany.lab2missionanalyzerpatterns;

import com.mycompany.lab2missionanalyzerpatterns.parser.*;
import com.mycompany.lab2missionanalyzerpatterns.report.*;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParserRegistry registry = new ParserRegistry();
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".json"), new JsonMissionParser());
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".yaml") || f.getName().toLowerCase().endsWith(".yml"), new YamlMissionParser());
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".xml"), new XmlMissionParser());
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".txt"), new TxtMissionParser());
        // fallback для всех остальных (в том числе A5) будет в самом реестре

        System.out.println("Выберите тип отчета: 1 - Краткий, 2 - Детальный");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        ReportGenerator reportGenerator = (choice == 1) ? new SummaryReportGenerator() : new DetailedReportGenerator();

        System.out.println("Введите путь к файлу или папке:");
        String path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Путь не существует");
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) processFile(f, registry, reportGenerator);
                }
            }
        } else {
            processFile(file, registry, reportGenerator);
        }
    }

    private static void processFile(File file, ParserRegistry registry, ReportGenerator reportGenerator) {
        System.out.println("\n--- Обработка: " + file.getName() + " ---");
        try {
            MissionParser parser = registry.getParser(file); // теперь fallback сработает
            var mission = parser.parse(file);
            reportGenerator.generate(mission);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке: " + e.getMessage());
            e.printStackTrace();
        }
    }
}