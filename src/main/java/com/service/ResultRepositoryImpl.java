package com.service;

import com.domain.Result;
import com.domain.ViewDTO;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResultRepositoryImpl implements ResultRepository {

    private EntityManager fm;

    public ResultRepositoryImpl(EntityManager em) {
        this.fm = em;
    }

    @Override
    public void trackResults(List<Result> result) {
        if (result.size() > 0) {
            for (Result r : result) {
                Result res = new Result(r.getRouteId(), r.isExist(), r.getLength());
                fm.getTransaction().begin();
                fm.persist(res);
                fm.flush();
                fm.getTransaction().commit();
            }
        }
    }

    @Override
    public void trackOneResult(Result result) {
        fm.getTransaction().begin();
        fm.persist(result);
        fm.flush();
        fm.getTransaction().commit();
    }

    @Override
    public List<Result> getCombinationsResults() {
        return fm.createQuery("select r from Result r ")
                .getResultList();
    }

    @Override
    public void clearCyclicResults() {
        fm.getTransaction().begin();
        fm.createQuery("delete from WaterPipeLineSystem w " +
                "where w.idX=w.idY ").executeUpdate();
        fm.getTransaction().commit();
        fm.getTransaction().begin();
        fm.createQuery("delete from Route w " +
                "where w.idA=w.idB ").executeUpdate();
        fm.getTransaction().commit();
    }

    @Override
    public Set<ViewDTO> resultsView() {
        List<Result> res = getCombinationsResults();
        Set<ViewDTO> dto = new HashSet<>();
        res.forEach(result -> dto.add(new ViewDTO(result.getRouteId(), result.isExist(), result.getLength())));
        return dto;
    }


}
