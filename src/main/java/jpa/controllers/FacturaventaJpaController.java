/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Facturaventa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Pagoventa;
import entidades.Ordenventa;
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
public class FacturaventaJpaController implements Serializable {

    public FacturaventaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facturaventa facturaventa) throws RollbackFailureException, Exception {
        if (facturaventa.getOrdenventaCollection() == null) {
            facturaventa.setOrdenventaCollection(new ArrayList<Ordenventa>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pagoventa pagoid = facturaventa.getPagoid();
            if (pagoid != null) {
                pagoid = em.getReference(pagoid.getClass(), pagoid.getPagoventaid());
                facturaventa.setPagoid(pagoid);
            }
            Collection<Ordenventa> attachedOrdenventaCollection = new ArrayList<Ordenventa>();
            for (Ordenventa ordenventaCollectionOrdenventaToAttach : facturaventa.getOrdenventaCollection()) {
                ordenventaCollectionOrdenventaToAttach = em.getReference(ordenventaCollectionOrdenventaToAttach.getClass(), ordenventaCollectionOrdenventaToAttach.getOrdenventaid());
                attachedOrdenventaCollection.add(ordenventaCollectionOrdenventaToAttach);
            }
            facturaventa.setOrdenventaCollection(attachedOrdenventaCollection);
            em.persist(facturaventa);
            if (pagoid != null) {
                pagoid.getFacturaventaCollection().add(facturaventa);
                pagoid = em.merge(pagoid);
            }
            for (Ordenventa ordenventaCollectionOrdenventa : facturaventa.getOrdenventaCollection()) {
                Facturaventa oldFacturaidOfOrdenventaCollectionOrdenventa = ordenventaCollectionOrdenventa.getFacturaid();
                ordenventaCollectionOrdenventa.setFacturaid(facturaventa);
                ordenventaCollectionOrdenventa = em.merge(ordenventaCollectionOrdenventa);
                if (oldFacturaidOfOrdenventaCollectionOrdenventa != null) {
                    oldFacturaidOfOrdenventaCollectionOrdenventa.getOrdenventaCollection().remove(ordenventaCollectionOrdenventa);
                    oldFacturaidOfOrdenventaCollectionOrdenventa = em.merge(oldFacturaidOfOrdenventaCollectionOrdenventa);
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

    public void edit(Facturaventa facturaventa) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Facturaventa persistentFacturaventa = em.find(Facturaventa.class, facturaventa.getFacturaventaid());
            Pagoventa pagoidOld = persistentFacturaventa.getPagoid();
            Pagoventa pagoidNew = facturaventa.getPagoid();
            Collection<Ordenventa> ordenventaCollectionOld = persistentFacturaventa.getOrdenventaCollection();
            Collection<Ordenventa> ordenventaCollectionNew = facturaventa.getOrdenventaCollection();
            List<String> illegalOrphanMessages = null;
            for (Ordenventa ordenventaCollectionOldOrdenventa : ordenventaCollectionOld) {
                if (!ordenventaCollectionNew.contains(ordenventaCollectionOldOrdenventa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenventa " + ordenventaCollectionOldOrdenventa + " since its facturaid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pagoidNew != null) {
                pagoidNew = em.getReference(pagoidNew.getClass(), pagoidNew.getPagoventaid());
                facturaventa.setPagoid(pagoidNew);
            }
            Collection<Ordenventa> attachedOrdenventaCollectionNew = new ArrayList<Ordenventa>();
            for (Ordenventa ordenventaCollectionNewOrdenventaToAttach : ordenventaCollectionNew) {
                ordenventaCollectionNewOrdenventaToAttach = em.getReference(ordenventaCollectionNewOrdenventaToAttach.getClass(), ordenventaCollectionNewOrdenventaToAttach.getOrdenventaid());
                attachedOrdenventaCollectionNew.add(ordenventaCollectionNewOrdenventaToAttach);
            }
            ordenventaCollectionNew = attachedOrdenventaCollectionNew;
            facturaventa.setOrdenventaCollection(ordenventaCollectionNew);
            facturaventa = em.merge(facturaventa);
            if (pagoidOld != null && !pagoidOld.equals(pagoidNew)) {
                pagoidOld.getFacturaventaCollection().remove(facturaventa);
                pagoidOld = em.merge(pagoidOld);
            }
            if (pagoidNew != null && !pagoidNew.equals(pagoidOld)) {
                pagoidNew.getFacturaventaCollection().add(facturaventa);
                pagoidNew = em.merge(pagoidNew);
            }
            for (Ordenventa ordenventaCollectionNewOrdenventa : ordenventaCollectionNew) {
                if (!ordenventaCollectionOld.contains(ordenventaCollectionNewOrdenventa)) {
                    Facturaventa oldFacturaidOfOrdenventaCollectionNewOrdenventa = ordenventaCollectionNewOrdenventa.getFacturaid();
                    ordenventaCollectionNewOrdenventa.setFacturaid(facturaventa);
                    ordenventaCollectionNewOrdenventa = em.merge(ordenventaCollectionNewOrdenventa);
                    if (oldFacturaidOfOrdenventaCollectionNewOrdenventa != null && !oldFacturaidOfOrdenventaCollectionNewOrdenventa.equals(facturaventa)) {
                        oldFacturaidOfOrdenventaCollectionNewOrdenventa.getOrdenventaCollection().remove(ordenventaCollectionNewOrdenventa);
                        oldFacturaidOfOrdenventaCollectionNewOrdenventa = em.merge(oldFacturaidOfOrdenventaCollectionNewOrdenventa);
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
                Long id = facturaventa.getFacturaventaid();
                if (findFacturaventa(id) == null) {
                    throw new NonexistentEntityException("The facturaventa with id " + id + " no longer exists.");
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
            Facturaventa facturaventa;
            try {
                facturaventa = em.getReference(Facturaventa.class, id);
                facturaventa.getFacturaventaid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaventa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ordenventa> ordenventaCollectionOrphanCheck = facturaventa.getOrdenventaCollection();
            for (Ordenventa ordenventaCollectionOrphanCheckOrdenventa : ordenventaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facturaventa (" + facturaventa + ") cannot be destroyed since the Ordenventa " + ordenventaCollectionOrphanCheckOrdenventa + " in its ordenventaCollection field has a non-nullable facturaid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pagoventa pagoid = facturaventa.getPagoid();
            if (pagoid != null) {
                pagoid.getFacturaventaCollection().remove(facturaventa);
                pagoid = em.merge(pagoid);
            }
            em.remove(facturaventa);
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

    public List<Facturaventa> findFacturaventaEntities() {
        return findFacturaventaEntities(true, -1, -1);
    }

    public List<Facturaventa> findFacturaventaEntities(int maxResults, int firstResult) {
        return findFacturaventaEntities(false, maxResults, firstResult);
    }

    private List<Facturaventa> findFacturaventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facturaventa.class));
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

    public Facturaventa findFacturaventa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facturaventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facturaventa> rt = cq.from(Facturaventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
