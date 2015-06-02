package com.iblagojevic.dao;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component("genericDao")
public class GenericDao<T extends Serializable, PK extends Serializable>
        implements Serializable, IGenericDao<T, PK> {

    /**
     *
     */
    private static final long serialVersionUID = 3157480407337774799L;

    EntityManager entityManager;
    public Class<T> entity;

    @SuppressWarnings("unchecked")
    public GenericDao() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            this.entity = (Class<T>) paramType.getActualTypeArguments()[0];
        } else if (type instanceof Class) {
            this.entity = (Class<T>) type;
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void persist(Object transientInstance) {
        try {
            entityManager.persist(transientInstance);
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH PERSIST METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
    }

    @Override
    public void remove(PK id, Class<T> clazz) {
        try {
            entityManager.remove(entityManager.getReference(clazz, id));
            entityManager.flush();
            entityManager.clear();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH REMOVE METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
    }

    @Override
    public T merge(T detachedInstance) {
        try {
            return entityManager.merge(detachedInstance);
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH MERGE METHOD: "
                    + re.getMessage());
            re.printStackTrace();
            return null;
        }
    }

    @Override
    public T detach(T persistedInstance) {
        try {
            entityManager.detach(persistedInstance);
            return persistedInstance;
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH DETACH METHOD: "
                    + re.getMessage());
            re.printStackTrace();
            return null;
        }
    }

    @Override
    public T findById(PK id, Class<T> clazz) {
        T instance = null;
        try {
            instance = entityManager.find(clazz, id);
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH FINDBYID METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
        return instance;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(String hql) {
        List<T> result = null;
        try {
            result = entityManager.createQuery(hql).getResultList();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH FINDBYCRITERIA METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
        return result;
    }

    @Override
    public Long count(String hql) {
        Long result = 0L;
        try {
            result = (Long) entityManager.createQuery(hql).getSingleResult();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH COUNT METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByCriteriaWithLimit(String hql, int start, int limit) {
        List<T> result = null;
        try {
            result = entityManager.createQuery(hql)
                    .setFirstResult(start).setMaxResults(limit).getResultList();
        } catch (RuntimeException re) {
            System.out
                    .println("GENERIC DAO ERROR WITH FINDBYCRITERIA WITH LIMIT METHOD: "
                            + re.getMessage());
            re.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByCriteriaParameters(String hql, Map<String, ?> paramMap) {
        Query query = entityManager.createQuery(hql);
        for (Map.Entry<String, ?> param : paramMap.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        try {
            return query.getResultList();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO PARAMETERS ERROR METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByCriteriaParametersWithLimits(String hql, Map<String, ?> paramMap,
                                                      int firstResult, int maxResults) {
        Query query = this.getEntityManager().createQuery(hql);
        for (Map.Entry<String, ?> param : paramMap.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        try {
            return query.getResultList();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH CUSTOM SEARCH WITH LIMIT METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
        return null;
    }

    @Override
    public Number customCount(String hql, Map<String, ?> paramMap) {
        Query query = entityManager.createQuery(hql);
        for (Map.Entry<String, ?> param : paramMap.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        try {
            return (Number) query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public <R> List<R> findScalarByCriteria(String hql, Class<R> clazz) {
        TypedQuery<R> query = entityManager.createQuery(hql, clazz);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByCriteriaUsingNativeQuery(String sql, Class<T> clazz) {
        List<T> result = null;
        try {
            result = entityManager.createNativeQuery(sql, clazz).getResultList();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH FINDBYCRITERIA METHOD: "
                    + re.getMessage());
            re.printStackTrace();
        }
        return result;
    }

    @Override
    public int executeNonRetrievalNativeQuery(String sql) {
        try {
            return entityManager.createNativeQuery(sql).executeUpdate();
        } catch (RuntimeException re) {
            System.out.println("GENERIC DAO ERROR WITH NATIVE SQL QUERY (" + sql + "): "
                    + re.getMessage());
            re.printStackTrace();
            return 0;
        }
    }

}
