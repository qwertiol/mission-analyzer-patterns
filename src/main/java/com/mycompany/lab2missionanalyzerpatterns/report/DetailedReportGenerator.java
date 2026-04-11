package com.mycompany.lab2missionanalyzerpatterns.report;

import com.mycompany.lab2missionanalyzerpatterns.model.*;

public class DetailedReportGenerator implements ReportGenerator {
    @Override
    public void generate(Mission mission) {
        System.out.println("~~~ Детальный отчет о миссии ~~~");
        System.out.println("ID миссии: " + mission.getMissionId());
        System.out.println("Дата: " + mission.getDate());
        System.out.println("Место: " + mission.getLocation());
        System.out.println("Исход: " + mission.getOutcome());
        System.out.println("Ущерб: " + mission.getDamageCost());
        System.out.println("Проклятие: " + mission.getCurse().getName() + " (уровень: " + mission.getCurse().getThreatLevel() + ")");
        
        System.out.println("Участники:");
        for (Sorcerer s : mission.getSorcerers()) {
            System.out.println("  - " + s.getName() + " (ранг: " + s.getRank() + ")");
        }
        System.out.println("Примененные техники:");
        for (Technique t : mission.getTechniques()) {
            System.out.println("  - " + t.getName() + " (тип: " + t.getType() + ", владелец: " + t.getOwner() + ", урон: " + t.getDamage() + ")");
        }
        
        if (mission.getEconomicAssessment() != null) {
            EconomicAssessment ea = mission.getEconomicAssessment();
            System.out.println("Экономическая оценка:");
            System.out.println("  Общий ущерб: " + ea.getTotalDamageCost());
            System.out.println("  Инфраструктура: " + ea.getInfrastructureDamage());
            System.out.println("  Коммерческий: " + ea.getCommercialDamage());
            System.out.println("  Транспорт: " + ea.getTransportDamage());
            System.out.println("  Дни восстановления: " + ea.getRecoveryEstimateDays());
            System.out.println("  Страховое покрытие: " + ea.getInsuranceCovered());
        }
        
        if (mission.getEnemyActivity() != null) {
            EnemyActivity ea = mission.getEnemyActivity();
            System.out.println("Активность врага:");
            System.out.println("  Тип поведения: " + ea.getBehaviorType());
            System.out.println("  Мобильность: " + ea.getMobility());
            if (ea.getAttackPatterns() != null)
                System.out.println("  Паттерны атак: " + String.join(", ", ea.getAttackPatterns()));
        }
        
        if (mission.getEnvironmentConditions() != null) {
            EnvironmentConditions ec = mission.getEnvironmentConditions();
            System.out.println("Условия среды: погода=" + ec.getWeather() + ", видимость=" + ec.getVisibility() + ", плотность энергии=" + ec.getCursedEnergyDensity());
        }
        
        if (mission.getCivilianImpact() != null) {
            CivilianImpact ci = mission.getCivilianImpact();
            System.out.println("Влияние на гражданских: эвакуировано=" + ci.getEvacuated() + ", пострадало=" + ci.getInjured() + ", пропало=" + ci.getMissing());
        }
        
        if (mission.getTimelineEvents() != null && !mission.getTimelineEvents().isEmpty()) {
            System.out.println("Хронология:");
            for (TimelineEvent e : mission.getTimelineEvents()) {
                System.out.println("  " + e.getTimestamp() + " - " + e.getType() + ": " + e.getDescription());
            }
        }

        if (mission.getComment() != null) System.out.println("Комментарий: " + mission.getComment());
    }
}
