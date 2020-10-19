package com.service;

import com.domain.Result;
import com.domain.Route;

import java.util.List;

public interface RouteRepo {

    List<Route> showAllRoutes();

    int findQuantityOfRoutes();

    List<Result> findRoutesWhichAreFailedAfterCombination();

    List<Result> findRoutesWhichAreDirectlySucceeded();
}
