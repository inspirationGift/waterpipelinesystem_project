package com.service;

import com.domain.Combinations;
import com.domain.Result;

import java.util.List;

public interface CombinationsService {
    void initComboTable(int version);

    List<Combinations> findAll();

    void deleteVersion(int version);

    boolean analyse(int version);

    List<Result> getResult(int version);

}
