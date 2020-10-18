package com.service;

import com.domain.Result;
import com.domain.Route;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ResultRepositoryImpl implements ResultRepository {

    private EntityManager fm;

    public ResultRepositoryImpl(EntityManager em) {
        this.fm = em;
    }

    @Override
    public void trackListResults(List<Result> result) {
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
    public void trackResult(Result result) {
        fm.getTransaction().begin();
        fm.persist(result);
        fm.flush();
        fm.getTransaction().commit();
    }

    @Override
    public void checkCyclic() {
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
    public List<Route> findAllRoutes() {
        return fm.createQuery("select r from Route r ").getResultList();
    }

    @Override
    public int totalRoutes() {
        Query q;
        q = fm.createQuery("select max(r.id) from Route r"); // !!!todo null pointer
        List resultList = q.getResultList();
        if (resultList.size() != 0)
            return (int) resultList.get(0);
        else return 0;
    }

    @Override
    public void routesFailed() {
        Query query;
        query = fm.createQuery(
                "select r from Route r " +
                        "where r.id not in(" +
                        "select distinct m.routeId from Result m)");

        List<Route> resultList = query.getResultList();
        if (resultList.size() > 0)
            for (Route r : resultList) trackResult(new Result(r.getId(), false, 0));
    }

    @Override
    public void routesSuccess() {
        Query query = fm.createQuery("select a.id as routeId, r.length " +
                "from WaterPipeLineSystem r " +
                "inner join Route a on r.idX=a.idA and r.idY=a.idB ");
        List<Object[]> resultList = query.getResultList();

        resultList.forEach(v -> {
            Result res = new Result((int) v[0], true, (int) v[1]);
            trackResult(res);
        });

        // todo failed scenario
    }

    @Override
    public List<Route> getResult() {
        Query q;
        q = fm.createQuery("SELECT r from Result r order by r.routeId asc");
        List<Route> resultList = q.getResultList();
        return resultList;
    }

}
