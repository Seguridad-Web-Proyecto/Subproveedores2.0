/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Cargo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Historialtrabajo;
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
public class CargoJpaController implements Serializable {

    public CargoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargo cargo) throws RollbackFailureException, Exception {
        if (cargo.getHistorialtrabajoCollection() == null) {
            cargo.setHistorialtrabajoCollection(new ArrayList<Historialtrabajo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Historialtrabajo> attachedHistorialtrabajoCollection = new ArrayList<Historialtrabajo>();
            for (Historialtrabajo historialtrabajoCollectionHistorialtrabajoToAttach : cargo.getHistorialtrabajoCollection()) {
                historialtrabajoCollectionHistorialtrabajoToAttach = em.getReference(historialtrabajoCollectionHistorialtrabajoToAttach.getClass(), historialtrabajoCollectionHistorialtrabajoToAttach.getHistorialid());
                attachedHistorialtrabajoCollection.add(historialtrabajoCollectionHistorialtrabajoToAttach);
            }
            cargo.setHistorialtrabajoCollection(attachedHistorialtrabajoCollection);
            em.persist(cargo);
            for (Historialtrabajo historialtrabajoCollectionHistorialtrabajo : cargo.getHistorialtrabajoCollection()) {
                Cargo oldCargoidOfHistorialtrabajoCollectionHistorialtrabajo = historialtrabajoCollectionHistorialtrabajo.getCargoid();
                historialtrabajoCollectionHistorialtrabajo.setCargoid(cargo);
                historialtrabajoCollectionHistorialtrabajo = em.merge(historialtrabajoCollectionHistorialtrabajo);
                if (oldCargoidOfHistorialtrabajoCollectionHistorialtrabajo != null) {
                    oldCargoidOfHistorialtrabajoCollectionHistorialtrabajo.getHistorialtrabajoCollection().remove(historialtrabajoCollectionHistorialtrabajo);
                    oldCargoidOfHistorialtrabajoCollectionHistorialtrabajo = em.merge(oldCargoidOfHistorialtrabajoCollectionHistorialtrabajo);
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

    public void edit(Cargo cargo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo persistentCargo = em.find(Cargo.class, cargo.getCargoid());
            Collection<Historialtrabajo> historialtrabajoCollectionOld = persistentCargo.getHistorialtrabajoCollection();
            Collection<Historialtrabajo> historialtrabajoCollectionNew = cargo.getHistorialtrabajoCollection();
            List<String> illegalOrphanMessages = null;
            for (Historialtrabajo historialtrabajoCollectionOldHistorialtrabajo : historialtrabajoCollectionOld) {
                if (!historialtrabajoCollectionNew.contains(historialtrabajoCollectionOldHistorialtrabajo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historialtrabajo " + historialtrabajoCollectionOldHistorialtrabajo + " since its cargoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Historialtrabajo> attachedHistorialtrabajoCollectionNew = new ArrayList<Historialtrabajo>();
            for (Historialtrabajo historialtrabajoCollectionNewHistorialtrabajoToAttach : historialtrabajoCollectionNew) {
                historialtrabajoCollectionNewHistorialtrabajoToAttach = em.getReference(historialtrabajoCollectionNewHistorialtrabajoToAttach.getClass(), historialtrabajoCollectionNewHistorialtrabajoToAttach.getHistorialid());
                attachedHistorialtrabajoCollectionNew.add(historialtrabajoCollectionNewHistorialtrabajoToAttach);
            }
            historialtrabajoCollectionNew = attachedHistorialtrabajoCollectionNew;
            cargo.setHistorialtrabajoCollection(historialtrabajoCollectionNew);
            cargo = em.merge(cargo);
            for (Historialtrabajo historialtrabajoCollectionNewHistorialtrabajo : historialtrabajoCollectionNew) {
                if (!historialtrabajoCollectionOld.contains(historialtrabajoCollectionNewHistorialtrabajo)) {
                    Cargo oldCargoidOfHistorialtrabajoCollectionNewHistorialtrabajo = historialtrabajoCollectionNewHistorialtrabajo.getCargoid();
                    historialtrabajoCollectionNewHistorialtrabajo.setCargoid(cargo);
                    historialtrabajoCollectionNewHistorialtrabajo = em.merge(historialtrabajoCollectionNewHistorialtrabajo);
                    if (oldCargoidOfHistorialtrabajoCollectionNewHistorialtrabajo != null && !oldCargoidOfHistorialtrabajoCollectionNewHistorialtrabajo.equals(cargo)) {
                        oldCargoidOfHistorialtrabajoCollectionNewHistorialtrabajo.getHistorialtrabajoCollection().remove(historialtrabajoCollectionNewHistorialtrabajo);
                        oldCargoidOfHistorialtrabajoCollectionNewHistorialtrabajo = em.merge(oldCargoidOfHistorialtrabajoCollectionNewHistorialtrabajo);
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
                Long id = cargo.getCargoid();
                if (findCargo(id) == null) {
                    throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.");
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
            Cargo cargo;
            try {
                cargo = em.getReference(Cargo.class, id);
                cargo.getCargoid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Historialtrabajo> historialtrabajoCollectionOrphanCheck = cargo.getHistorialtrabajoCollection();
            for (Historialtrabajo historialtrabajoCollectionOrphanCheckHistorialtrabajo : historialtrabajoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargo (" + cargo + ") cannot be destroyed since the Historialtrabajo " + historialtrabajoCollectionOrphanCheckHistorialtrabajo + " in its historialtrabajoCollection field has a non-nullable cargoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargo);
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

    public List<Cargo> findCargoEntities() {
        return findCargoEntities(true, -1, -1);
    }

    public List<Cargo> findCargoEntities(int maxResults, int firstResult) {
        return findCargoEntities(false, maxResults, firstResult);
    }

    private List<Cargo> findCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargo.class));
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

    public Cargo findCargo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargo> rt = cq.from(Cargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
