# generic-dao
Provides Hibernate- and Spring-based DAO layer using generics, so it can be used with any Hibernate domain object in Spring environment without the need to create dedicated data access interfaces for each domain class.

Examples of usages:

```
...
import your.domain.class.User
...

@Autowired
IGenericDao<User, Integer> dao;

// to get array of objects
@Autowired
IGenericDao<Object[], Integer> objectDao;

public User getUser(Integer id) {
  return dao.findById(id, User.class);
}

public List<User> getUsersByCompany(Integer companyId) {
    String query = "select u from User u where u.company.id = :companyId";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("companyId", companyId);
    return dao.findByCriteriaParameters(query, params);
}

public List<Object[]> getUserListWithCount(int companyId) {
    String query = "SELECT COUNT(u), u FROM User u where u.company.id = :companyId GROUP BY u.company";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("companyId", companyId);
    return objectDao.findByCriteriaParameters(query, params)
}

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
public User saveOrUpdateUser(User user) {
    if (user.getId() == null) {
        dao.persist(user);
        return user;
    } else {
        return dao.merge(user);
    }
}

...

```

