/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Facturacompra;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Pagocompra;
import entidades.Ordencompra;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jaker
 */
public class FacturacompraJpaController implements Serializable {

    public FacturacompraJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facturacompra facturacompra) throws RollbackFailureException, Exception {
        if (facturacompra.getOrdencompraCollection() == null) {
            facturacompra.setOrdencompraCollection(new ArrayList<Ordencompra>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pagocompra pagoid = facturacompra.getPagoid();
            if (pagoid != null) {
                pagoid = em.getReference(pagoid.getClass(), pagoid.getPagocompraid());
                facturacompra.setPagoid(pagoid);
            }
            Collection<Ordencompra> attachedOrdencompraCollection = new ArrayList<Ordencompra>();
            for (Ordencompra ordencompraCollectionOrdencompraToAttach : facturacompra.getOrdencompraCollection()) {
                ordencompraCollectionOrdencompraToAttach = em.getReference(ordencompraCollectionOrdencompraToAttach.getClass(), ordencompraCollectionOrdencompraToAttach.getOrdencompraid());
                attachedOrdencompraCollection.add(ordencompraCollectionOrdencompraToAttach);
            }
            facturacompra.setOrdencompraCollection(attachedOrdencompraCollection);
            em.persist(facturacompra);
            if (pagoid != null) {
                pagoid.getFacturacompraCollection().add(facturacompra);
                pagoid = em.merge(pagoid);
            }
            for (Ordencompra ordencompraCollectionOrdencompra : facturacompra.getOrdencompraCollection()) {
                Facturacompra oldFacturaidOfOrdencompraCollectionOrdencompra = ordencompraCollectionOrdencompra.getFacturaid();
                ordencompraCollectionOrdencompra.setFacturaid(facturacompra);
                ordencompraCollectionOrdencompra = em.merge(ordencompraCollectionOrdencompra);
                if (oldFacturaidOfOrdencompraCollectionOrdencompra != null) {
                    oldFacturaidOfOrdencompraCollectionOrdencompra.getOrdencompraCollection().remove(ordencompraCollectionOrdencompra);
                    oldFacturaidOfOrdencompraCollectionOrdencompra = em.merge(oldFacturaidOfOrdencompraCollectionOrdencompra);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Facturacompra facturacompra) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Facturacompra persistentFacturacompra = em.find(Facturacompra.class, facturacompra.getFacturacompraid());
            Pagocompra pagoidOld = persistentFacturacompra.getPagoid();
            Pagocompra pagoidNew = facturacompra.getPagoid();
            Collection<Ordencompra> ordencompraCollectionOld = persistentFacturacompra.getOrdencompraCollection();
            Collection<Ordencompra> ordencompraCollectionNew = facturacompra.getOrdencompraCollection();
            List<String> illegalOrphanMessages = null;
            for (Ordencompra ordencompraCollectionOldOrdencompra : ordencompraCollectionOld) {
                if (!ordencompraCollectionNew.contains(ordencompraCollectionOldOrdencompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordencompra " + ordencompraCollectionOldOrdencompra + " since its facturaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoidNew != null) {
                pagoidNew = em.getReference(pagoidNew.getClass(), pagoidNew.getPagocompraid());
                facturacompra.setPagoid(pagoidNew);
            }
            Collection<Ordencompra> attachedOrdencompraCollectionNew = new ArrayList<Ordencompra>();
            for (Ordencompra ordencompraCollectionNewOrdencompraToAttach : ordencompraCollectionNew) {
                ordencompraCollectionNewOrdencompraToAttach = em.getReference(ordencompraCollectionNewOrdencompraToAttach.getClass(), ordencompraCollectionNewOrdencompraToAttach.getOrdencompraid());
                attachedOrdencompraCollectionNew.add(ordencompraCollectionNewOrdencompraToAttach);
            }
            ordencompraCollectionNew = attachedOrdencompraCollectionNew;
            facturacompra.setOrdencompraCollection(ordencompraCollectionNew);
            facturacompra = em.merge(facturacompra);
            if (pagoidOld != null && !pagoidOld.equals(pagoidNew)) {
                pagoidOld.getFacturacompraCollection().remove(facturacompra);
                pagoidOld = em.merge(pagoidOld);
            }
            if (pagoidNew != null && !pagoidNew.equals(pagoidOld)) {
                pagoidNew.getFacturacompraCollection().add(facturacompra);
                pagoidNew = em.merge(pagoidNew);
            }
            for (Ordencompra ordencompraCollectionNewOrdencompra : ordencompraCollectionNew) {
                if (!ordencompraCollectionOld.contains(ordencompraCollectionNewOrdencompra)) {
                    Facturacompra oldFacturaidOfOrdencompraCollectionNewOrdencompra = ordencompraCollectionNewOrdencompra.getFacturaid();
                    ordencompraCollectionNewOrdencompra.setFacturaid(facturacompra);
                    ordencompraCollectionNewOrdencompra = em.merge(ordencompraCollectionNewOrdencompra);
                    if (oldFacturaidOfOrdencompraCollectionNewOrdencompra != null && !oldFacturaidOfOrdencompraCollectionNewOrdencompra.equals(facturacompra)) {
                        oldFacturaidOfOrdencompraCollectionNewOrdencompra.getOrdencompraCollection().remove(ordencompraCollectionNewOrdencompra);
                        oldFacturaidOfOrdencompraCollectionNewOrdencompra = em.merge(oldFacturaidOfOrdencompraCollectionNewOrdencompra);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = facturacompra.getFacturacompraid();
                if (findFacturacompra(id) == null) {
                    throw new NonexistentEntityException("The facturacompra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Facturacompra facturacompra;
            try {
                facturacompra = em.getReference(Facturacompra.class, id);
                facturacompra.getFacturacompraid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturacompra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ordencompra> ordencompraCollectionOrphanCheck = facturacompra.getOrdencompraCollection();
            for (Ordencompra ordencompraCollectionOrphanCheckOrdencompra : ordencompraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facturacompra (" + facturacompra + ") cannot be destroyed since the Ordencompra " + ordencompraCollectionOrphanCheckOrdencompra + " in its ordencompraCollection field has a non-nullable facturaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pagocompra pagoid = facturacompra.getPagoid();
            if (pagoid != null) {
                pagoid.getFacturacompraCollection().remove(facturacompra);
                pagoid = em.merge(pagoid);
            }
            em.remove(facturacompra);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Facturacompra> findFacturacompraEntities() {
        return findFacturacompraEntities(true, -1, -1);
    }

    public List<Facturacompra> findFacturacompraEntities(int maxResults, int firstResult) {
        return findFacturacompraEntities(false, maxResults, firstResult);
    }

    private List<Facturacompra> findFacturacompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facturacompra.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Facturacompra findFacturacompra(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facturacompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturacompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facturacompra> rt = cq.from(Facturacompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
