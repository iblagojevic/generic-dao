package com.iblagojevic.dao;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface IGenericDao<T extends Serializable, PK extends Serializable> {

    public void persist(Object transientInstance);

    public void remove(PK id, Class<T> clazz);

    public T merge(T detachedInstance);

    public T detach(T persistedInstance);

    public T findById(PK id, Class<T> clazz);

    public List<T> findByCriteria(String hql);

    public Long count(String hql);

    public List<T> findByCriteriaWithLimit(String hql, int start, int limit);

    public List<T> findByCriteriaParameters(String hql, Map<String, ?> paramMap);

    public List<T> findByCriteriaParametersWithLimits(String hql, Map<String, ?> paramMap, int firstResult, int maxResults);

    public Number customCount(String hql, Map<String, ?> paramMap);

    public <R> List<R> findScalarByCriteria(String hql, Class<R> clazz);

    public List<T> findByCriteriaUsingNativeQuery(String sql, Class<T> clazz);

    public int executeNonRetrievalNativeQuery(String sql);
}
