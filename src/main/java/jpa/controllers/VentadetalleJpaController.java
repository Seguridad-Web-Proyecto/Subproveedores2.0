/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Ordenventa;
import entidades.Producto;
import entidades.Ventadetalle;
import entidades.VentadetallePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jaker
 */
public class VentadetalleJpaController implements Serializable {

    public VentadetalleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventadetalle ventadetalle) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (ventadetalle.getVentadetallePK() == null) {
            ventadetalle.setVentadetallePK(new VentadetallePK());
        }
        ventadetalle.getVentadetallePK().setProductoid(ventadetalle.getProducto().getProductoid());
        ventadetalle.getVentadetallePK().setVentaid(ventadetalle.getOrdenventa().getOrdenventaid());
        EntityManager em = null;
        try {
            //utx.begin();
            em = getEntityManager();
            Ordenventa ordenventa = ventadetalle.getOrdenventa();
            if (ordenventa != null) {
                ordenventa = em.getReference(ordenventa.getClass(), ordenventa.getOrdenventaid());
                ventadetalle.setOrdenventa(ordenventa);
            }
            Producto producto = ventadetalle.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getProductoid());
                ventadetalle.setProducto(producto);
            }
            em.persist(ventadetalle);
            if (ordenventa != null) {
                ordenventa.getVentadetalleCollection().add(ventadetalle);
                ordenventa = em.merge(ordenventa);
            }
            if (producto != null) {
                producto.getVentadetalleCollection().add(ventadetalle);
                producto = em.merge(producto);
            }
            //utx.commit();
        } catch (Exception ex) {
            try {
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findVentadetalle(ventadetalle.getVentadetallePK()) != null) {
                throw new PreexistingEntityException("Ventadetalle " + ventadetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventadetalle ventadetalle) throws NonexistentEntityException, RollbackFailureException, Exception {
        ventadetalle.getVentadetallePK().setProductoid(ventadetalle.getProducto().getProductoid());
        ventadetalle.getVentadetallePK().setVentaid(ventadetalle.getOrdenventa().getOrdenventaid());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ventadetalle persistentVentadetalle = em.find(Ventadetalle.class, ventadetalle.getVentadetallePK());
            Ordenventa ordenventaOld = persistentVentadetalle.getOrdenventa();
            Ordenventa ordenventaNew = ventadetalle.getOrdenventa();
            Producto productoOld = persistentVentadetalle.getProducto();
            Producto productoNew = ventadetalle.getProducto();
            if (ordenventaNew != null) {
                ordenventaNew = em.getReference(ordenventaNew.getClass(), ordenventaNew.getOrdenventaid());
                ventadetalle.setOrdenventa(ordenventaNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getProductoid());
                ventadetalle.setProducto(productoNew);
            }
            ventadetalle = em.merge(ventadetalle);
            if (ordenventaOld != null && !ordenventaOld.equals(ordenventaNew)) {
                ordenventaOld.getVentadetalleCollection().remove(ventadetalle);
                ordenventaOld = em.merge(ordenventaOld);
            }
            if (ordenventaNew != null && !ordenventaNew.equals(ordenventaOld)) {
                ordenventaNew.getVentadetalleCollection().add(ventadetalle);
                ordenventaNew = em.merge(ordenventaNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getVentadetalleCollection().remove(ventadetalle);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getVentadetalleCollection().add(ventadetalle);
                productoNew = em.merge(productoNew);
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
                VentadetallePK id = ventadetalle.getVentadetallePK();
                if (findVentadetalle(id) == null) {
                    throw new NonexistentEntityException("The ventadetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(VentadetallePK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ventadetalle ventadetalle;
            try {
                ventadetalle = em.getReference(Ventadetalle.class, id);
                ventadetalle.getVentadetallePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventadetalle with id " + id + " no longer exists.", enfe);
            }
            Ordenventa ordenventa = ventadetalle.getOrdenventa();
            if (ordenventa != null) {
                ordenventa.getVentadetalleCollection().remove(ventadetalle);
                ordenventa = em.merge(ordenventa);
            }
            Producto producto = ventadetalle.getProducto();
            if (producto != null) {
                producto.getVentadetalleCollection().remove(ventadetalle);
                producto = em.merge(producto);
            }
            em.remove(ventadetalle);
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

    public List<Ventadetalle> findVentadetalleEntities() {
        return findVentadetalleEntities(true, -1, -1);
    }

    public List<Ventadetalle> findVentadetalleEntities(int maxResults, int firstResult) {
        return findVentadetalleEntities(false, maxResults, firstResult);
    }

    private List<Ventadetalle> findVentadetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventadetalle.class));
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

    public Ventadetalle findVentadetalle(VentadetallePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventadetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentadetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventadetalle> rt = cq.from(Ventadetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
