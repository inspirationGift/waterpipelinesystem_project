package com;

import com.domain.Result;
import com.domain.Route;
import com.domain.WaterPipeLineSystem;
import com.percistence.FactoryManager;
import com.service.*;

import javax.persistence.EntityManager;
import java.util.List;

public class Worker {

    private String sysWaterFile;
    private String pointsFile;
    private String outputFolder;
    private EntityManager em;
    private CsvProcessing csv;
    private ResultRepository findResults;
    private ComboService combo;

    public Worker(String sysWaterFile, String pointsFile, String outputFolder) {
        this.sysWaterFile = sysWaterFile;
        this.pointsFile = pointsFile;
        this.outputFolder = outputFolder;
        em = FactoryManager.getEntityManager();
        csv = new CsvProcessingImpl(em);
        findResults = new ResultRepositoryImpl(em);
        combo = new ComboTableImpl(em);
    }

    public void checkRoutes() {
        try {
            csv.readCsv(sysWaterFile, WaterPipeLineSystem.class);
            csv.readCsv(pointsFile, Route.class);

            findResults.routesSuccess();
            findResults.checkCyclic();

            boolean go = true;
            int i = 0;

            while (go) {
                combo.initComboTable(i);
                go = combo.analyse(i);
                List<Result> result = combo.getResult(i);
                findResults.trackListResults(result);
                i++;
            }
            findResults.routesFailed();
            csv.writeCsv(outputFolder); // output
            System.out.println("Please check results");
        } catch (
                Exception e) {
            e.printStackTrace();
            //todo
        }
    }


}
