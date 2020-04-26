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
import entidades.Pagocompra;
import entidades.Tarjetacreditocompra;
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
public class TarjetacreditocompraJpaController implements Serializable {

    public TarjetacreditocompraJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjetacreditocompra tarjetacreditocompra) throws RollbackFailureException, Exception {
        if (tarjetacreditocompra.getPagocompraCollection() == null) {
            tarjetacreditocompra.setPagocompraCollection(new ArrayList<Pagocompra>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Pagocompra> attachedPagocompraCollection = new ArrayList<Pagocompra>();
            for (Pagocompra pagocompraCollectionPagocompraToAttach : tarjetacreditocompra.getPagocompraCollection()) {
                pagocompraCollectionPagocompraToAttach = em.getReference(pagocompraCollectionPagocompraToAttach.getClass(), pagocompraCollectionPagocompraToAttach.getPagocompraid());
                attachedPagocompraCollection.add(pagocompraCollectionPagocompraToAttach);
            }
            tarjetacreditocompra.setPagocompraCollection(attachedPagocompraCollection);
            em.persist(tarjetacreditocompra);
            for (Pagocompra pagocompraCollectionPagocompra : tarjetacreditocompra.getPagocompraCollection()) {
                Tarjetacreditocompra oldTarjetacreditoidOfPagocompraCollectionPagocompra = pagocompraCollectionPagocompra.getTarjetacreditoid();
                pagocompraCollectionPagocompra.setTarjetacreditoid(tarjetacreditocompra);
                pagocompraCollectionPagocompra = em.merge(pagocompraCollectionPagocompra);
                if (oldTarjetacreditoidOfPagocompraCollectionPagocompra != null) {
                    oldTarjetacreditoidOfPagocompraCollectionPagocompra.getPagocompraCollection().remove(pagocompraCollectionPagocompra);
                    oldTarjetacreditoidOfPagocompraCollectionPagocompra = em.merge(oldTarjetacreditoidOfPagocompraCollectionPagocompra);
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

    public void edit(Tarjetacreditocompra tarjetacreditocompra) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tarjetacreditocompra persistentTarjetacreditocompra = em.find(Tarjetacreditocompra.class, tarjetacreditocompra.getTarjetacreditocompraid());
            Collection<Pagocompra> pagocompraCollectionOld = persistentTarjetacreditocompra.getPagocompraCollection();
            Collection<Pagocompra> pagocompraCollectionNew = tarjetacreditocompra.getPagocompraCollection();
            List<String> illegalOrphanMessages = null;
            for (Pagocompra pagocompraCollectionOldPagocompra : pagocompraCollectionOld) {
                if (!pagocompraCollectionNew.contains(pagocompraCollectionOldPagocompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pagocompra " + pagocompraCollectionOldPagocompra + " since its tarjetacreditoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pagocompra> attachedPagocompraCollectionNew = new ArrayList<Pagocompra>();
            for (Pagocompra pagocompraCollectionNewPagocompraToAttach : pagocompraCollectionNew) {
                pagocompraCollectionNewPagocompraToAttach = em.getReference(pagocompraCollectionNewPagocompraToAttach.getClass(), pagocompraCollectionNewPagocompraToAttach.getPagocompraid());
                attachedPagocompraCollectionNew.add(pagocompraCollectionNewPagocompraToAttach);
            }
            pagocompraCollectionNew = attachedPagocompraCollectionNew;
            tarjetacreditocompra.setPagocompraCollection(pagocompraCollectionNew);
            tarjetacreditocompra = em.merge(tarjetacreditocompra);
            for (Pagocompra pagocompraCollectionNewPagocompra : pagocompraCollectionNew) {
                if (!pagocompraCollectionOld.contains(pagocompraCollectionNewPagocompra)) {
                    Tarjetacreditocompra oldTarjetacreditoidOfPagocompraCollectionNewPagocompra = pagocompraCollectionNewPagocompra.getTarjetacreditoid();
                    pagocompraCollectionNewPagocompra.setTarjetacreditoid(tarjetacreditocompra);
                    pagocompraCollectionNewPagocompra = em.merge(pagocompraCollectionNewPagocompra);
                    if (oldTarjetacreditoidOfPagocompraCollectionNewPagocompra != null && !oldTarjetacreditoidOfPagocompraCollectionNewPagocompra.equals(tarjetacreditocompra)) {
                        oldTarjetacreditoidOfPagocompraCollectionNewPagocompra.getPagocompraCollection().remove(pagocompraCollectionNewPagocompra);
                        oldTarjetacreditoidOfPagocompraCollectionNewPagocompra = em.merge(oldTarjetacreditoidOfPagocompraCollectionNewPagocompra);
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
                Long id = tarjetacreditocompra.getTarjetacreditocompraid();
                if (findTarjetacreditocompra(id) == null) {
                    throw new NonexistentEntityException("The tarjetacreditocompra with id " + id + " no longer exists.");
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
            Tarjetacreditocompra tarjetacreditocompra;
            try {
                tarjetacreditocompra = em.getReference(Tarjetacreditocompra.class, id);
                tarjetacreditocompra.getTarjetacreditocompraid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetacreditocompra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pagocompra> pagocompraCollectionOrphanCheck = tarjetacreditocompra.getPagocompraCollection();
            for (Pagocompra pagocompraCollectionOrphanCheckPagocompra : pagocompraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarjetacreditocompra (" + tarjetacreditocompra + ") cannot be destroyed since the Pagocompra " + pagocompraCollectionOrphanCheckPagocompra + " in its pagocompraCollection field has a non-nullable tarjetacreditoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarjetacreditocompra);
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

    public List<Tarjetacreditocompra> findTarjetacreditocompraEntities() {
        return findTarjetacreditocompraEntities(true, -1, -1);
    }

    public List<Tarjetacreditocompra> findTarjetacreditocompraEntities(int maxResults, int firstResult) {
        return findTarjetacreditocompraEntities(false, maxResults, firstResult);
    }

    private List<Tarjetacreditocompra> findTarjetacreditocompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjetacreditocompra.class));
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

    public Tarjetacreditocompra findTarjetacreditocompra(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjetacreditocompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetacreditocompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjetacreditocompra> rt = cq.from(Tarjetacreditocompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
