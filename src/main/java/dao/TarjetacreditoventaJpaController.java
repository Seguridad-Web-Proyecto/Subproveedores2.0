/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Pagoventa;
import entidades.Tarjetacreditoventa;
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
public class TarjetacreditoventaJpaController implements Serializable {

    public TarjetacreditoventaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjetacreditoventa tarjetacreditoventa) throws RollbackFailureException, Exception {
        if (tarjetacreditoventa.getPagoventaCollection() == null) {
            tarjetacreditoventa.setPagoventaCollection(new ArrayList<Pagoventa>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Pagoventa> attachedPagoventaCollection = new ArrayList<Pagoventa>();
            for (Pagoventa pagoventaCollectionPagoventaToAttach : tarjetacreditoventa.getPagoventaCollection()) {
                pagoventaCollectionPagoventaToAttach = em.getReference(pagoventaCollectionPagoventaToAttach.getClass(), pagoventaCollectionPagoventaToAttach.getPagoventaid());
                attachedPagoventaCollection.add(pagoventaCollectionPagoventaToAttach);
            }
            tarjetacreditoventa.setPagoventaCollection(attachedPagoventaCollection);
            em.persist(tarjetacreditoventa);
            for (Pagoventa pagoventaCollectionPagoventa : tarjetacreditoventa.getPagoventaCollection()) {
                Tarjetacreditoventa oldTarjetacreditoidOfPagoventaCollectionPagoventa = pagoventaCollectionPagoventa.getTarjetacreditoid();
                pagoventaCollectionPagoventa.setTarjetacreditoid(tarjetacreditoventa);
                pagoventaCollectionPagoventa = em.merge(pagoventaCollectionPagoventa);
                if (oldTarjetacreditoidOfPagoventaCollectionPagoventa != null) {
                    oldTarjetacreditoidOfPagoventaCollectionPagoventa.getPagoventaCollection().remove(pagoventaCollectionPagoventa);
                    oldTarjetacreditoidOfPagoventaCollectionPagoventa = em.merge(oldTarjetacreditoidOfPagoventaCollectionPagoventa);
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

    public void edit(Tarjetacreditoventa tarjetacreditoventa) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tarjetacreditoventa persistentTarjetacreditoventa = em.find(Tarjetacreditoventa.class, tarjetacreditoventa.getTarjetacreditoventaid());
            Collection<Pagoventa> pagoventaCollectionOld = persistentTarjetacreditoventa.getPagoventaCollection();
            Collection<Pagoventa> pagoventaCollectionNew = tarjetacreditoventa.getPagoventaCollection();
            List<String> illegalOrphanMessages = null;
            for (Pagoventa pagoventaCollectionOldPagoventa : pagoventaCollectionOld) {
                if (!pagoventaCollectionNew.contains(pagoventaCollectionOldPagoventa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pagoventa " + pagoventaCollectionOldPagoventa + " since its tarjetacreditoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pagoventa> attachedPagoventaCollectionNew = new ArrayList<Pagoventa>();
            for (Pagoventa pagoventaCollectionNewPagoventaToAttach : pagoventaCollectionNew) {
                pagoventaCollectionNewPagoventaToAttach = em.getReference(pagoventaCollectionNewPagoventaToAttach.getClass(), pagoventaCollectionNewPagoventaToAttach.getPagoventaid());
                attachedPagoventaCollectionNew.add(pagoventaCollectionNewPagoventaToAttach);
            }
            pagoventaCollectionNew = attachedPagoventaCollectionNew;
            tarjetacreditoventa.setPagoventaCollection(pagoventaCollectionNew);
            tarjetacreditoventa = em.merge(tarjetacreditoventa);
            for (Pagoventa pagoventaCollectionNewPagoventa : pagoventaCollectionNew) {
                if (!pagoventaCollectionOld.contains(pagoventaCollectionNewPagoventa)) {
                    Tarjetacreditoventa oldTarjetacreditoidOfPagoventaCollectionNewPagoventa = pagoventaCollectionNewPagoventa.getTarjetacreditoid();
                    pagoventaCollectionNewPagoventa.setTarjetacreditoid(tarjetacreditoventa);
                    pagoventaCollectionNewPagoventa = em.merge(pagoventaCollectionNewPagoventa);
                    if (oldTarjetacreditoidOfPagoventaCollectionNewPagoventa != null && !oldTarjetacreditoidOfPagoventaCollectionNewPagoventa.equals(tarjetacreditoventa)) {
                        oldTarjetacreditoidOfPagoventaCollectionNewPagoventa.getPagoventaCollection().remove(pagoventaCollectionNewPagoventa);
                        oldTarjetacreditoidOfPagoventaCollectionNewPagoventa = em.merge(oldTarjetacreditoidOfPagoventaCollectionNewPagoventa);
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
                Long id = tarjetacreditoventa.getTarjetacreditoventaid();
                if (findTarjetacreditoventa(id) == null) {
                    throw new NonexistentEntityException("The tarjetacreditoventa with id " + id + " no longer exists.");
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
            Tarjetacreditoventa tarjetacreditoventa;
            try {
                tarjetacreditoventa = em.getReference(Tarjetacreditoventa.class, id);
                tarjetacreditoventa.getTarjetacreditoventaid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetacreditoventa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pagoventa> pagoventaCollectionOrphanCheck = tarjetacreditoventa.getPagoventaCollection();
            for (Pagoventa pagoventaCollectionOrphanCheckPagoventa : pagoventaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarjetacreditoventa (" + tarjetacreditoventa + ") cannot be destroyed since the Pagoventa " + pagoventaCollectionOrphanCheckPagoventa + " in its pagoventaCollection field has a non-nullable tarjetacreditoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarjetacreditoventa);
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

    public List<Tarjetacreditoventa> findTarjetacreditoventaEntities() {
        return findTarjetacreditoventaEntities(true, -1, -1);
    }

    public List<Tarjetacreditoventa> findTarjetacreditoventaEntities(int maxResults, int firstResult) {
        return findTarjetacreditoventaEntities(false, maxResults, firstResult);
    }

    private List<Tarjetacreditoventa> findTarjetacreditoventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjetacreditoventa.class));
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

    public Tarjetacreditoventa findTarjetacreditoventa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjetacreditoventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetacreditoventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjetacreditoventa> rt = cq.from(Tarjetacreditoventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
