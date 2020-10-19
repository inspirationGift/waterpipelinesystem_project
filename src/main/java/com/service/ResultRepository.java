package com.service;

import com.domain.Result;
import com.domain.ViewDTO;

import java.util.List;
import java.util.Set;

public interface ResultRepository {

    void trackOneResult(Result result);

    void trackResults(List<Result> result);

    void clearCyclicResults();

    List<Result> getCombinationsResults();

    Set<ViewDTO> resultsView();
}
