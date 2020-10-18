package com.service;

import com.domain.Combinations;
import com.domain.Result;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ComboTableImpl implements ComboService {

    private EntityManager em;

    public ComboTableImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void initComboTable(int version) {
        String s;
        if (version == 0)
            s = "insert into Combinations(routeId,x,y,mid,len,version) " +
                    "SELECT a.id, a.idA, a.idB, b.idY mid, b.length len, " + version + " FROM route a " +
                    "left join  WaterPipeLineSystem b on a.idA=b.idX " +
                    "where a.idA<>a.idB ";
        else
            s = "insert into Combinations(routeId,x,y,mid,len,version) " +
                    "SELECT a.routeId, a.x, a.y, b.idY mid, a.len + b.length len, " + version + " FROM Combinations a " +
                    "left join  WaterPipeLineSystem b on a.mid=b.idX ";

        em.getTransaction().begin();
        Query query;
        query = em.createNativeQuery(s);
        query.executeUpdate();
        em.getTransaction().commit();

        deleteVersion(version - 1);
        //todo rollback
    }

    @Override
    public List<Combinations> findAll() {
        List<Combinations> list = em.createQuery("select r from Combinations r ").getResultList();
        return list;
        //todo rollback
    }

    @Override
    public boolean analyse(int version) {
        List resultList = em.createQuery("select r from Combinations r where r.mid is not null and version=" + version).getResultList();
        if (resultList.size() == 0) return false;
        return true;
    }

    @Override
    public List<Result> getResult(int version) {
        Query query;
        query = em.createQuery("select r from Combinations r " +
                "where r.y=r.mid  and version=" + version);
        List<Combinations> resultList = query.getResultList();
        List<Result> results = new ArrayList<>();
        for (Combinations c : resultList) results.add(castComboToResult(c));
        return results;
    }

    @Override
    public void deleteVersion(int version) {
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("delete from Combinations where version=" + version);
            query.executeUpdate();
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //todo rollback
    }

    private Result castComboToResult(Combinations combo) {
        return new Result(combo.getRouteId(), true, combo.getLen());
    }

}
