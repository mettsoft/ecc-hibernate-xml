package com.ecc.hibernate_xml.dao;

import java.util.List;
import org.hibernate.criterion.Order;

import com.ecc.hibernate_xml.model.Person;
import com.ecc.hibernate_xml.util.dao.TransactionScope;
import com.ecc.hibernate_xml.util.dao.HibernateUtility;

public class PersonDao extends AbstractDao<Person> {

	public PersonDao() {
		super(Person.class);
	}

	public List<Person> listByDateHired() {		
		return TransactionScope.executeTransactionWithResult(session -> 
			session.createQuery("FROM Person ORDER BY dateHired").setCacheable(true).list());
	}

	public List<Person> listByLastName() {
		return TransactionScope.executeTransactionWithResult(session -> {
			return session.createCriteria(Person.class)
				.setCacheable(true)
				.addOrder(Order.asc("name.lastName"))
				.list();
		});
	}

	@Override
	public void delete(Person person) throws DaoException {
		super.delete(person);
		HibernateUtility.getSessionFactory().getCache().evictCollection(
			"com.ecc.hibernate_xml.model.Person.roles", person.getId());
	}
}