package com.mycompany.lab2missionanalyzerpatterns.report;

import com.mycompany.lab2missionanalyzerpatterns.model.Mission;

public interface ReportGenerator {
    void generate(Mission mission);
}