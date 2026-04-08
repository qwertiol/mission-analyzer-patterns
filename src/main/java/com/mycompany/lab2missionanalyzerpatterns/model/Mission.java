package com.mycompany.lab2missionanalyzerpatterns.model;

import java.time.LocalDate;
import java.util.List;

public class Mission {
    // обязательные поля
    private String missionId;
    private LocalDate date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;
    private List<Sorcerer> sorcerers;
    private List<Technique> techniques;

    // опциональные дополнительные блоки
    private EconomicAssessment economicAssessment;
    private EnemyActivity enemyActivity;
    private EnvironmentConditions environmentConditions;
    private CivilianImpact civilianImpact;
    private List<TimelineEvent> timelineEvents;
    private String comment; // для заметок

    // Приватный конструктор для Builder
    private Mission() {}

    // Все геттеры (сеттеры не нужны, объект неизменяем после построения)
    public String getMissionId() { return missionId; }
    public LocalDate getDate() { return date; }
    public String getLocation() { return location; }
    public String getOutcome() { return outcome; }
    public long getDamageCost() { return damageCost; }
    public Curse getCurse() { return curse; }
    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public List<Technique> getTechniques() { return techniques; }
    public EconomicAssessment getEconomicAssessment() { return economicAssessment; }
    public EnemyActivity getEnemyActivity() { return enemyActivity; }
    public EnvironmentConditions getEnvironmentConditions() { return environmentConditions; }
    public CivilianImpact getCivilianImpact() { return civilianImpact; }
    public List<TimelineEvent> getTimelineEvents() { return timelineEvents; }
    public String getComment() { return comment; }

    // Вложенный Builder
    public static class Builder {
        private final Mission mission = new Mission();

        public Builder missionId(String missionId) { mission.missionId = missionId; return this; }
        public Builder date(LocalDate date) { mission.date = date; return this; }
        public Builder location(String location) { mission.location = location; return this; }
        public Builder outcome(String outcome) { mission.outcome = outcome; return this; }
        public Builder damageCost(long damageCost) { mission.damageCost = damageCost; return this; }
        public Builder curse(Curse curse) { mission.curse = curse; return this; }
        public Builder sorcerers(List<Sorcerer> sorcerers) { mission.sorcerers = sorcerers; return this; }
        public Builder techniques(List<Technique> techniques) { mission.techniques = techniques; return this; }
        public Builder economicAssessment(EconomicAssessment economicAssessment) { mission.economicAssessment = economicAssessment; return this; }
        public Builder enemyActivity(EnemyActivity enemyActivity) { mission.enemyActivity = enemyActivity; return this; }
        public Builder environmentConditions(EnvironmentConditions environmentConditions) { mission.environmentConditions = environmentConditions; return this; }
        public Builder civilianImpact(CivilianImpact civilianImpact) { mission.civilianImpact = civilianImpact; return this; }
        public Builder timelineEvents(List<TimelineEvent> timelineEvents) { mission.timelineEvents = timelineEvents; return this; }
        public Builder comment(String comment) { mission.comment = comment; return this; }

        public Mission build() {
            // минимальная валидация
            if (mission.missionId == null || mission.date == null || mission.location == null ||
                mission.outcome == null || mission.curse == null) {
                throw new IllegalStateException("Missing required mission fields");
            }
            if (mission.sorcerers == null) mission.sorcerers = List.of();
            if (mission.techniques == null) mission.techniques = List.of();
            return mission;
        }
    }
}