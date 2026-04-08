package com.mycompany.lab2missionanalyzerpatterns;

import com.mycompany.lab2missionanalyzerpatterns.parser.*;
import com.mycompany.lab2missionanalyzerpatterns.report.*;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Инициализация реестра парсеров (расширяемый)
        ParserRegistry registry = new ParserRegistry();
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".json"), new JsonMissionParser());
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".yaml") || f.getName().toLowerCase().endsWith(".yml"), new YamlMissionParser());
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".xml"), new XmlMissionParser());
        registry.registerParser(f -> f.getName().toLowerCase().endsWith(".txt") && !f.getName().contains("A5"), new TxtMissionParser());
        registry.registerParser(f -> f.getName().contains("A5") || f.getName().toLowerCase().endsWith(".custom"), new CustomMissionParser());

        // Выбор типа отчета (паттерн Стратегия)
        System.out.println("Выберите тип отчета: 1 - Краткий, 2 - Детальный");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        ReportGenerator reportGenerator;
        if (choice == 1) reportGenerator = new SummaryReportGenerator();
        else reportGenerator = new DetailedReportGenerator();

        System.out.println("Введите путь к файлу или папке:");
        String path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Путь не существует");
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".json") || name.toLowerCase().endsWith(".yaml") ||
                name.toLowerCase().endsWith(".xml") || name.toLowerCase().endsWith(".txt"));
            if (files != null) {
                for (File f : files) {
                    processFile(f, registry, reportGenerator);
                }
            }
        } else {
            processFile(file, registry, reportGenerator);
        }
    }

    private static void processFile(File file, ParserRegistry registry, ReportGenerator reportGenerator) {
        System.out.println("\n--- Обработка: " + file.getName() + " ---");
        try {
            MissionParser parser = registry.getParser(file);
            var mission = parser.parse(file);
            reportGenerator.generate(mission);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке: " + e.getMessage());
        }
    }
}
