package com.mycompany.lab2missionanalyzerpatterns.parser;

import com.mycompany.lab2missionanalyzerpatterns.builder.MissionBuilder;
import com.mycompany.lab2missionanalyzerpatterns.model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws IOException {
        MissionBuilder builder = new MissionBuilder();
        List<Sorcerer> sorcerers = new ArrayList<>();
        List<Technique> techniques = new ArrayList<>();
        EnvironmentConditions env = new EnvironmentConditions();
        String currentSection = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq == -1) continue;
                String key = line.substring(0, eq).trim();
                String value = line.substring(eq + 1).trim();

                if (currentSection == null || currentSection.equals("MISSION")) {
                    switch (key) {
                        case "missionId": builder.setMissionId(value); break;
                        case "date": builder.setDate(LocalDate.parse(value)); break;
                        case "location": builder.setLocation(value); break;
                        case "outcome": builder.setOutcome(value); break;
                        case "damageCost": builder.setDamageCost(Long.parseLong(value)); break;
                    }
                } else if (currentSection.equals("CURSE")) {
                    Curse curse = new Curse();
                    if (key.equals("name")) curse.setName(value);
                    else if (key.equals("threatLevel")) curse.setThreatLevel(value);
                    builder.setCurse(curse);
                } else if (currentSection.equals("SORCERER")) {
                    if (key.equals("name") || key.equals("rank")) {
                        Sorcerer last = sorcerers.isEmpty() ? null : sorcerers.get(sorcerers.size() - 1);
                        if (last == null || (last.getName() != null && last.getRank() != null)) {
                            last = new Sorcerer();
                            sorcerers.add(last);
                        }
                        if (key.equals("name")) last.setName(value);
                        else if (key.equals("rank")) last.setRank(value);
                    }
                } else if (currentSection.equals("TECHNIQUE")) {
                    Technique t = new Technique();
                    t.setName(key.equals("name") ? value : null);
                    t.setType(key.equals("type") ? value : null);
                    t.setOwner(key.equals("owner") ? value : null);
                    if (key.equals("damage")) t.setDamage(Long.parseLong(value));
                    techniques.add(t);
                } else if (currentSection.equals("ENVIRONMENT")) {
                    switch (key) {
                        case "weather": env.setWeather(value); break;
                        case "timeOfDay": env.setTimeOfDay(value); break;
                        case "visibility": env.setVisibility(value); break;
                        case "cursedEnergyDensity": env.setCursedEnergyDensity(Integer.parseInt(value)); break;
                    }
                }
            }
        }
        builder.setSorcerers(sorcerers);
        builder.setTechniques(techniques);
        if (env.getWeather() != null || env.getTimeOfDay() != null || env.getVisibility() != null || env.getCursedEnergyDensity() != null)
            builder.setEnvironmentConditions(env);
        return builder.build();
    }
}