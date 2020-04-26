/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Ganancia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jaker
 */
public class GananciaJpaController implements Serializable {

    public GananciaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ganancia ganancia) throws IllegalOrphanException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        Producto productoidOrphanCheck = ganancia.getProductoid();
        if (productoidOrphanCheck != null) {
            Ganancia oldGananciaOfProductoid = productoidOrphanCheck.getGanancia();
            if (oldGananciaOfProductoid != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Producto " + productoidOrphanCheck + " already has an item of type Ganancia whose productoid column cannot be null. Please make another selection for the productoid field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto productoid = ganancia.getProductoid();
            if (productoid != null) {
                productoid = em.getReference(productoid.getClass(), productoid.getProductoid());
                ganancia.setProductoid(productoid);
            }
            em.persist(ganancia);
            if (productoid != null) {
                productoid.setGanancia(ganancia);
                productoid = em.merge(productoid);
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

    public void edit(Ganancia ganancia) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ganancia persistentGanancia = em.find(Ganancia.class, ganancia.getGananciaid());
            Producto productoidOld = persistentGanancia.getProductoid();
            Producto productoidNew = ganancia.getProductoid();
            List<String> illegalOrphanMessages = null;
            if (productoidNew != null && !productoidNew.equals(productoidOld)) {
                Ganancia oldGananciaOfProductoid = productoidNew.getGanancia();
                if (oldGananciaOfProductoid != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Producto " + productoidNew + " already has an item of type Ganancia whose productoid column cannot be null. Please make another selection for the productoid field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (productoidNew != null) {
                productoidNew = em.getReference(productoidNew.getClass(), productoidNew.getProductoid());
                ganancia.setProductoid(productoidNew);
            }
            ganancia = em.merge(ganancia);
            if (productoidOld != null && !productoidOld.equals(productoidNew)) {
                productoidOld.setGanancia(null);
                productoidOld = em.merge(productoidOld);
            }
            if (productoidNew != null && !productoidNew.equals(productoidOld)) {
                productoidNew.setGanancia(ganancia);
                productoidNew = em.merge(productoidNew);
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
                Long id = ganancia.getGananciaid();
                if (findGanancia(id) == null) {
                    throw new NonexistentEntityException("The ganancia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ganancia ganancia;
            try {
                ganancia = em.getReference(Ganancia.class, id);
                ganancia.getGananciaid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ganancia with id " + id + " no longer exists.", enfe);
            }
            Producto productoid = ganancia.getProductoid();
            if (productoid != null) {
                productoid.setGanancia(null);
                productoid = em.merge(productoid);
            }
            em.remove(ganancia);
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

    public List<Ganancia> findGananciaEntities() {
        return findGananciaEntities(true, -1, -1);
    }

    public List<Ganancia> findGananciaEntities(int maxResults, int firstResult) {
        return findGananciaEntities(false, maxResults, firstResult);
    }

    private List<Ganancia> findGananciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ganancia.class));
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

    public Ganancia findGanancia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ganancia.class, id);
        } finally {
            em.close();
        }
    }

    public int getGananciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ganancia> rt = cq.from(Ganancia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
