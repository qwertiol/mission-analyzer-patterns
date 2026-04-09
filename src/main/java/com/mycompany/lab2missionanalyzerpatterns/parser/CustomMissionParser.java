package com.mycompany.lab2missionanalyzerpatterns.parser;

import com.mycompany.lab2missionanalyzerpatterns.builder.MissionBuilder;
import com.mycompany.lab2missionanalyzerpatterns.model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomMissionParser implements MissionParser {
    @Override
    public Mission parse(File file) throws IOException {
        MissionBuilder builder = new MissionBuilder();
        List<Sorcerer> sorcerers = new ArrayList<>();
        List<Technique> techniques = new ArrayList<>();
        List<TimelineEvent> timeline = new ArrayList<>();
        CivilianImpact civilianImpact = null;
        EnemyActivity enemyActivity = null;
        String outcome = null;
        Long damageCost = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length == 0) continue;
                String type = parts[0];
                switch (type) {
                    case "MISSION_CREATED":
                        if (parts.length >= 4) {
                            builder.setMissionId(parts[1]);
                            builder.setDate(LocalDate.parse(parts[2]));
                            builder.setLocation(parts[3]);
                        }
                        break;
                    case "CURSE_DETECTED":
                        if (parts.length >= 3) {
                            builder.setCurse(new Curse(parts[1], parts[2]));
                        }
                        break;
                    case "SORCERER_ASSIGNED":
                        if (parts.length >= 3) {
                            sorcerers.add(new Sorcerer(parts[1], parts[2]));
                        }
                        break;
                    case "TECHNIQUE_USED":
                        if (parts.length >= 5) {
                            techniques.add(new Technique(parts[1], parts[2], parts[3], Long.parseLong(parts[4])));
                        }
                        break;
                    case "TIMELINE_EVENT":
                        if (parts.length >= 4) {
                            timeline.add(new TimelineEvent(LocalDateTime.parse(parts[1]), parts[2], parts[3]));
                        }
                        break;
                    case "ENEMY_ACTION":
                        if (parts.length >= 3) {
                            if (enemyActivity == null) enemyActivity = new EnemyActivity();
                            if (enemyActivity.getAttackPatterns() == null) enemyActivity.setAttackPatterns(new ArrayList<>());
                            enemyActivity.getAttackPatterns().add(parts[1] + ": " + parts[2]);
                        }
                        break;
                    case "CIVILIAN_IMPACT":
                        civilianImpact = new CivilianImpact();
                        for (int i = 1; i < parts.length; i++) {
                            String[] kv = parts[i].split("=");
                            if (kv.length == 2) {
                                switch (kv[0]) {
                                    case "evacuated": civilianImpact.setEvacuated(Integer.parseInt(kv[1])); break;
                                    case "injured": civilianImpact.setInjured(Integer.parseInt(kv[1])); break;
                                    case "missing": civilianImpact.setMissing(Integer.parseInt(kv[1])); break;
                                }
                            }
                        }
                        break;
                    case "MISSION_RESULT":
                        if (parts.length >= 2) {
                            outcome = parts[1];
                            if (parts.length >= 3 && parts[2].startsWith("damageCost=")) {
                                damageCost = Long.parseLong(parts[2].substring("damageCost=".length()));
                            }
                        }
                        break;
                }
            }
        }
        builder.setSorcerers(sorcerers);
        builder.setTechniques(techniques);
        builder.setTimelineEvents(timeline);
        if (civilianImpact != null) builder.setCivilianImpact(civilianImpact);
        if (enemyActivity != null) builder.setEnemyActivity(enemyActivity);
        if (outcome != null) builder.setOutcome(outcome);
        if (damageCost != null) builder.setDamageCost(damageCost);
        return builder.build();
    }
}