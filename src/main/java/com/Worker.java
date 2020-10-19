package com;

import com.domain.Result;
import com.domain.Route;
import com.domain.ViewDTO;
import com.domain.WaterPipeLineSystem;
import com.percistence.FactoryManager;
import com.service.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

public class Worker {

    private EntityManager em;

    private CsvProcessing csv;
    private RouteRepo routeService;
    private ResultRepository resultService;
    private CombinationsService combinationsBuilder;

    public Worker() {
        this.em = FactoryManager.getEntityManager();
        this.csv = new CsvProcessingImpl(em);
        this.routeService = new RouteRepoImpl(em);
        this.combinationsBuilder = new CombinationsTableImpl(em);
        this.resultService = new ResultRepositoryImpl(em);
    }

    public void loadFiles(String waterSystemFile, String SeekingPointsFile) {
        this.csv.readCsv(waterSystemFile, WaterPipeLineSystem.class);
        this.csv.readCsv(SeekingPointsFile, Route.class);
    }

    public void findRoutes() {
        try {

            List<Result> result1 = routeService.findRoutesWhichAreDirectlySucceeded();
            resultService.trackResults(result1);
            resultService.clearCyclicResults();

            boolean go = true;
            int i = 0;

            while (go) {
                combinationsBuilder.initComboTable(i);
                go = combinationsBuilder.analyse(i);

                List<Result> result = combinationsBuilder.getResult(i);

                resultService.trackResults(result);
                i++;
            }

            List<Result> result3 = routeService.findRoutesWhichAreFailedAfterCombination();
            resultService.trackResults(result3);

            // output
            System.out.println("Please check results:");
            Set<ViewDTO> res = resultService.resultsView();
            res.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
            //todo
        }
    }

    public void uploadResults(String address) {
        csv.writeCsv(address);
    }


}
