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
                            Curse curse = new Curse(parts[1], parts[2]);
                            builder.setCurse(curse);
                        }
                        break;
                    case "SORCERER_ASSIGNED":
                        if (parts.length >= 3) {
                            Sorcerer s = new Sorcerer(parts[1], parts[2]);
                            sorcerers.add(s);
                        }
                        break;
                    case "TECHNIQUE_USED":
                        if (parts.length >= 5) {
                            Technique t = new Technique(parts[1], parts[2], parts[3], Long.parseLong(parts[4]));
                            techniques.add(t);
                        }
                        break;
                    case "TIMELINE_EVENT":
                        if (parts.length >= 4) {
                            TimelineEvent event = new TimelineEvent(LocalDateTime.parse(parts[1]), parts[2], parts[3]);
                            timeline.add(event);
                        }
                        break;
                    case "ENEMY_ACTION":
                        if (parts.length >= 3) {
                            // упрощённо: собираем поведение в enemyActivity
                            EnemyActivity ea = builder.build().getEnemyActivity();
                            if (ea == null) ea = new EnemyActivity();
                            if (ea.getAttackPatterns() == null) ea.setAttackPatterns(new ArrayList<>());
                            ea.getAttackPatterns().add(parts[1] + ": " + parts[2]);
                            builder.setEnemyActivity(ea);
                        }
                        break;
                    case "CIVILIAN_IMPACT":
                        if (parts.length >= 2) {
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
                        }
                        break;
                    case "MISSION_RESULT":
                        if (parts.length >= 2) {
                            builder.setOutcome(parts[1]);
                            if (parts.length >= 3 && parts[2].startsWith("damageCost=")) {
                                String dmg = parts[2].substring("damageCost=".length());
                                builder.setDamageCost(Long.parseLong(dmg));
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
        return builder.build();
    }
}
