package com.service;

import com.domain.Result;
import com.domain.Route;

import java.util.List;

public interface ResultRepository {

    void trackListResults(List<Result> result);

    void trackResult(Result result);

    void checkCyclic();

    List<Route> findAllRoutes();

    int totalRoutes();

    void routesFailed();

    void routesSuccess();

    List<Route> getResult();

}
