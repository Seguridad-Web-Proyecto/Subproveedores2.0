/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cargo;
import entidades.Empleado;
import entidades.Historialtrabajo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jaker
 */
public class HistorialtrabajoJpaController implements Serializable {

    public HistorialtrabajoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historialtrabajo historialtrabajo) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo cargoid = historialtrabajo.getCargoid();
            if (cargoid != null) {
                cargoid = em.getReference(cargoid.getClass(), cargoid.getCargoid());
                historialtrabajo.setCargoid(cargoid);
            }
            Empleado empleadoid = historialtrabajo.getEmpleadoid();
            if (empleadoid != null) {
                empleadoid = em.getReference(empleadoid.getClass(), empleadoid.getEmpleadoid());
                historialtrabajo.setEmpleadoid(empleadoid);
            }
            em.persist(historialtrabajo);
            if (cargoid != null) {
                cargoid.getHistorialtrabajoCollection().add(historialtrabajo);
                cargoid = em.merge(cargoid);
            }
            if (empleadoid != null) {
                empleadoid.getHistorialtrabajoCollection().add(historialtrabajo);
                empleadoid = em.merge(empleadoid);
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

    public void edit(Historialtrabajo historialtrabajo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Historialtrabajo persistentHistorialtrabajo = em.find(Historialtrabajo.class, historialtrabajo.getHistorialid());
            Cargo cargoidOld = persistentHistorialtrabajo.getCargoid();
            Cargo cargoidNew = historialtrabajo.getCargoid();
            Empleado empleadoidOld = persistentHistorialtrabajo.getEmpleadoid();
            Empleado empleadoidNew = historialtrabajo.getEmpleadoid();
            if (cargoidNew != null) {
                cargoidNew = em.getReference(cargoidNew.getClass(), cargoidNew.getCargoid());
                historialtrabajo.setCargoid(cargoidNew);
            }
            if (empleadoidNew != null) {
                empleadoidNew = em.getReference(empleadoidNew.getClass(), empleadoidNew.getEmpleadoid());
                historialtrabajo.setEmpleadoid(empleadoidNew);
            }
            historialtrabajo = em.merge(historialtrabajo);
            if (cargoidOld != null && !cargoidOld.equals(cargoidNew)) {
                cargoidOld.getHistorialtrabajoCollection().remove(historialtrabajo);
                cargoidOld = em.merge(cargoidOld);
            }
            if (cargoidNew != null && !cargoidNew.equals(cargoidOld)) {
                cargoidNew.getHistorialtrabajoCollection().add(historialtrabajo);
                cargoidNew = em.merge(cargoidNew);
            }
            if (empleadoidOld != null && !empleadoidOld.equals(empleadoidNew)) {
                empleadoidOld.getHistorialtrabajoCollection().remove(historialtrabajo);
                empleadoidOld = em.merge(empleadoidOld);
            }
            if (empleadoidNew != null && !empleadoidNew.equals(empleadoidOld)) {
                empleadoidNew.getHistorialtrabajoCollection().add(historialtrabajo);
                empleadoidNew = em.merge(empleadoidNew);
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
                Long id = historialtrabajo.getHistorialid();
                if (findHistorialtrabajo(id) == null) {
                    throw new NonexistentEntityException("The historialtrabajo with id " + id + " no longer exists.");
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
            Historialtrabajo historialtrabajo;
            try {
                historialtrabajo = em.getReference(Historialtrabajo.class, id);
                historialtrabajo.getHistorialid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialtrabajo with id " + id + " no longer exists.", enfe);
            }
            Cargo cargoid = historialtrabajo.getCargoid();
            if (cargoid != null) {
                cargoid.getHistorialtrabajoCollection().remove(historialtrabajo);
                cargoid = em.merge(cargoid);
            }
            Empleado empleadoid = historialtrabajo.getEmpleadoid();
            if (empleadoid != null) {
                empleadoid.getHistorialtrabajoCollection().remove(historialtrabajo);
                empleadoid = em.merge(empleadoid);
            }
            em.remove(historialtrabajo);
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

    public List<Historialtrabajo> findHistorialtrabajoEntities() {
        return findHistorialtrabajoEntities(true, -1, -1);
    }

    public List<Historialtrabajo> findHistorialtrabajoEntities(int maxResults, int firstResult) {
        return findHistorialtrabajoEntities(false, maxResults, firstResult);
    }

    private List<Historialtrabajo> findHistorialtrabajoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historialtrabajo.class));
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

    public Historialtrabajo findHistorialtrabajo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historialtrabajo.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialtrabajoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historialtrabajo> rt = cq.from(Historialtrabajo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
