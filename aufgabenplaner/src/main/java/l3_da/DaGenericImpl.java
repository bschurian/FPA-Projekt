package l3_da;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import l4_dm.DmAufgabe;
import l4_dm.DmSchritt;

public class DaGenericImpl<E extends DmAufgabe> implements DaGeneric<E> {

	private final Class<E> managedClass;
	private final EntityManager entityManager;

	public DaGenericImpl(Class<E> managedClass, EntityManager entityManager) {
		this.managedClass = managedClass;
		this.entityManager = entityManager;
	}

	@Override
	public boolean save(E entity) {
		if (entityManager.contains(entity)) {
			return false;
		}
		entityManager.persist(entity);
		return true;
	}

	@Override
	public void delete(E entity) {
		if(entityManager.contains(entity)){
			entityManager.remove(entity);
		}
	}

	@Override
	public E find(long id) throws l3_da.DaGeneric.IdNotFoundExc {
		return entityManager.find(managedClass, id);
	}

	@Override
	public List<E> findAll() {
		final TypedQuery<E> q = entityManager.createQuery("SELECT o FROM "
				+ managedClass.getName() + " o ORDER BY id ASC",
				managedClass);
		final List<E> results = (List<E>) q.getResultList();
		return results;
	}

	@Override
	public List<E> findByField(String fieldName, Object fieldValue) {
		return null;
	}

	@Override
	public List<E> findByWhere(String whereClause, Object... args) {
		return null;
	}

	@Override
	public List<E> findByExample(E example) {
		return null;
	}

}
