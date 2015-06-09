# generic-dao
Provides DAO API using generics in JPA/HIbernate/Spring environment, so it can be used with any Hibernate POJO in such architecture without the need to create dedicated data access interfaces for each domain class.

Maven dependencies:

```
<dependency>
    <groupId>org.hibernate.javax.persistence</groupId>
    <artifactId>hibernate-jpa-2.0-api</artifactId>
    <version>1.0.1.Final</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>3.2.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-entitymanager</artifactId>
    <version>4.2.6.Final</version>
</dependency>

```

Examples of usages:

```
...
import your.domain.package.User
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

public List<Object[]> getUserCountPerCompany() {
    String query = "SELECT c, COUNT(u) FROM User u join u.company c GROUP BY c";
    return objectDao.findByCriteria(query)
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

