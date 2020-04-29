/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;

/**
 *
 * @author jcami
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;
    
    private UserTransaction utx;
    
    private final EntityManagerFactory entityManagerFactory = 
            Persistence.createEntityManagerFactory("com.mycompany_Subproveedores_war_1.0-SNAPSHOTPU");

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public Response create(T entity) {
        try{
            getEntityManager().persist(entity);
            return Response.ok().build();
        }catch(Exception ex){
            return Response.serverError().build();
        }
    }

    public Response edit(T entity) {
        try{
            getEntityManager().merge(entity);
            return Response.ok().build();
        }catch(Exception ex){
            return Response.serverError().build();
        }
    }

    public Response remove(T entity) {
        try{
            getEntityManager().remove(getEntityManager().merge(entity));
            return Response.ok().build();
        }catch(Exception ex){
            return Response.serverError().build();
        }
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    protected UserTransaction getUserTransaction(){
        return this.utx;
    }
    
    protected EntityManagerFactory getEntityManagerFactory(){
        return this.entityManagerFactory;
    }
    
}
