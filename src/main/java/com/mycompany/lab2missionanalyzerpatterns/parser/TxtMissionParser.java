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
        Curse curse = null;
        String currentSection = null;
        Sorcerer currentSorcerer = null;
        Technique currentTechnique = null;

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
                    if (curse == null) curse = new Curse();
                    if (key.equals("name")) curse.setName(value);
                    else if (key.equals("threatLevel")) curse.setThreatLevel(value);
                } else if (currentSection.equals("SORCERER")) {
                    if (currentSorcerer == null) {
                        currentSorcerer = new Sorcerer();
                        sorcerers.add(currentSorcerer);
                    }
                    if (key.equals("name")) currentSorcerer.setName(value);
                    else if (key.equals("rank")) currentSorcerer.setRank(value);
                    if (currentSorcerer.getName() != null && currentSorcerer.getRank() != null) {
                        currentSorcerer = null; // готово, следующий SORCERER создаст нового
                    }
                } else if (currentSection.equals("TECHNIQUE")) {
                    if (currentTechnique == null) {
                        currentTechnique = new Technique();
                        techniques.add(currentTechnique);
                    }
                    switch (key) {
                        case "name": currentTechnique.setName(value); break;
                        case "type": currentTechnique.setType(value); break;
                        case "owner": currentTechnique.setOwner(value); break;
                        case "damage": currentTechnique.setDamage(Long.parseLong(value)); break;
                    }
                    // если все поля заполнены (хотя бы name, type, owner, damage не обязательны все), но для простоты:
                    if (currentTechnique.getName() != null && currentTechnique.getType() != null &&
                        currentTechnique.getOwner() != null && currentTechnique.getDamage() != 0) {
                        currentTechnique = null;
                    }
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
        if (curse != null) builder.setCurse(curse);
        builder.setSorcerers(sorcerers);
        builder.setTechniques(techniques);
        if (env.getWeather() != null || env.getTimeOfDay() != null || env.getVisibility() != null || env.getCursedEnergyDensity() != null)
            builder.setEnvironmentConditions(env);
        return builder.build();
    }
}