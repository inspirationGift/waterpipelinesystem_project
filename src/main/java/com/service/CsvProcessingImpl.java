package com.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;


import static java.lang.Integer.parseInt;

public class CsvProcessingImpl implements CsvProcessing {

    private EntityManager em;

    public CsvProcessingImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public <T> void readCsv(String file, Class<T> m) {
        String q = "SELECT * FROM CSVREAD('" + file + "', null ,'charset=UTF-8 fieldSeparator=' || char(9))";
        Query query;
        try {
            em.getTransaction().begin();
            query = em.createNativeQuery(q);
            query.getResultList().forEach(r -> {
                T mapper = mapper((String) r, m);
                em.persist(mapper);
            });
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            // catch error;
        }
    }

    @Override
    public void writeCsv(String folder) {
        Query query;
        this.em.getTransaction().begin();
        query = em.createNativeQuery("call CSVWRITE ( '" + folder +
                "MyCSVResult.csv', 'SELECT exist as `ROUTE EXISTS`, " +
                "min(length) as `MIN LENGTH` FROM RESULT group by exist, routeId " +
                "order by routeId' )");
        query.executeUpdate();
        em.getTransaction().commit();
    }

    public <T> T mapper(String str, Class<T> m) {
        String[] split = str.split("(,)|(;)|(\\|)|(\\s+)");
        Field[] fields = m.getDeclaredFields();
        T instance = null;
        try {
            Constructor<T> z = m.getConstructor();
            instance = z.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            // todo write exception handler
        }
        for (int i = 0; i < split.length; i++) {
            int x = parseInt(split[i]);
            try {
                fields[i + 1].setAccessible(true);
                fields[i + 1].set(instance, x);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                // todo write exception handler
            }
        }
        return instance;
    }
}
