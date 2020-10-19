package com.service;

import com.domain.Result;
import com.domain.Route;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class RouteRepoImpl implements RouteRepo {

    private EntityManager em;

    public RouteRepoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Route> showAllRoutes() {
        return em.createQuery("select distinct r from Route r ").getResultList();
    }

    @Override
    public int findQuantityOfRoutes() {
        Query q = em.createQuery("select max(r.id) from Route r"); // !!!todo null pointer
        List resultList = q.getResultList();
        if (resultList.size() != 0)
            return (int) resultList.get(0);
        else return 0;
    }

    @Override
    public List<Result> findRoutesWhichAreFailedAfterCombination() {
        Query query;
        query = em.createQuery(
                "select r from Route r " +
                        "where r.id not in(" +
                        "select distinct m.routeId from Result m)");

        List<Route> resultList = query.getResultList();

        List<Result> results = new ArrayList<>();
        if (resultList.size() > 0) {
            for (Route r : resultList) {
                results.add(new Result(r.getId(), false, 0));
            }
        }
        return results;
    }

    @Override
    public List<Result> findRoutesWhichAreDirectlySucceeded() {
        Query query = em.createQuery("select a.id as routeId, r.length " +
                "from WaterPipeLineSystem r " +
                "inner join Route a on r.idX=a.idA and r.idY=a.idB ");
        List<Object[]> resultList = query.getResultList();

        List<Result> results = new ArrayList<>();
        resultList.forEach(v -> {
            results.add(new Result((int) v[0], true, (int) v[1]));
        });
        return results;
        // todo failed scenario
    }
}
