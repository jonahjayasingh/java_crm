package com.jonahjayasingh.CRM.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DummyDataService dummyDataService;

    public DataInitializer(DummyDataService dummyDataService) {
        this.dummyDataService = dummyDataService;
    }

    @Override
    public void run(String... args) throws Exception {
        String report = dummyDataService.seedAllDummyData(false);
        System.out.println(report);
    }
}
