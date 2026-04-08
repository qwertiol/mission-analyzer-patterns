package com.mycompany.lab2missionanalyzerpatterns.report;

import com.mycompany.lab2missionanalyzerpatterns.model.*;

public class SummaryReportGenerator implements ReportGenerator {
    @Override
    public void generate(Mission mission) {
        System.out.println("~~~ Краткий отчет о миссии ~~~");
        System.out.println("ID: " + mission.getMissionId());
        System.out.println("Дата: " + mission.getDate());
        System.out.println("Локация: " + mission.getLocation());
        System.out.println("Исход: " + mission.getOutcome());
        System.out.println("Проклятие: " + mission.getCurse().getName() + " (уровень " + mission.getCurse().getThreatLevel() + ")");
        System.out.println("Участников: " + mission.getSorcerers().size());
        System.out.println("Техник применено: " + mission.getTechniques().size());
        if (mission.getEconomicAssessment() != null) {
            System.out.println("Экономический ущерб: " + mission.getEconomicAssessment().getTotalDamageCost());
        }
    }
}
