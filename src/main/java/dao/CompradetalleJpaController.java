/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Compradetalle;
import entidades.CompradetallePK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Ordencompra;
import entidades.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author jaker
 */
public class CompradetalleJpaController implements Serializable {

    public CompradetalleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compradetalle compradetalle) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (compradetalle.getCompradetallePK() == null) {
            compradetalle.setCompradetallePK(new CompradetallePK());
        }
        compradetalle.getCompradetallePK().setProductoid(compradetalle.getProducto().getProductoid());
        compradetalle.getCompradetallePK().setCompraid(compradetalle.getOrdencompra().getOrdencompraid());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ordencompra ordencompra = compradetalle.getOrdencompra();
            if (ordencompra != null) {
                ordencompra = em.getReference(ordencompra.getClass(), ordencompra.getOrdencompraid());
                compradetalle.setOrdencompra(ordencompra);
            }
            Producto producto = compradetalle.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getProductoid());
                compradetalle.setProducto(producto);
            }
            em.persist(compradetalle);
            if (ordencompra != null) {
                ordencompra.getCompradetalleCollection().add(compradetalle);
                ordencompra = em.merge(ordencompra);
            }
            if (producto != null) {
                producto.getCompradetalleCollection().add(compradetalle);
                producto = em.merge(producto);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCompradetalle(compradetalle.getCompradetallePK()) != null) {
                throw new PreexistingEntityException("Compradetalle " + compradetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compradetalle compradetalle) throws NonexistentEntityException, RollbackFailureException, Exception {
        compradetalle.getCompradetallePK().setProductoid(compradetalle.getProducto().getProductoid());
        compradetalle.getCompradetallePK().setCompraid(compradetalle.getOrdencompra().getOrdencompraid());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Compradetalle persistentCompradetalle = em.find(Compradetalle.class, compradetalle.getCompradetallePK());
            Ordencompra ordencompraOld = persistentCompradetalle.getOrdencompra();
            Ordencompra ordencompraNew = compradetalle.getOrdencompra();
            Producto productoOld = persistentCompradetalle.getProducto();
            Producto productoNew = compradetalle.getProducto();
            if (ordencompraNew != null) {
                ordencompraNew = em.getReference(ordencompraNew.getClass(), ordencompraNew.getOrdencompraid());
                compradetalle.setOrdencompra(ordencompraNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getProductoid());
                compradetalle.setProducto(productoNew);
            }
            compradetalle = em.merge(compradetalle);
            if (ordencompraOld != null && !ordencompraOld.equals(ordencompraNew)) {
                ordencompraOld.getCompradetalleCollection().remove(compradetalle);
                ordencompraOld = em.merge(ordencompraOld);
            }
            if (ordencompraNew != null && !ordencompraNew.equals(ordencompraOld)) {
                ordencompraNew.getCompradetalleCollection().add(compradetalle);
                ordencompraNew = em.merge(ordencompraNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getCompradetalleCollection().remove(compradetalle);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getCompradetalleCollection().add(compradetalle);
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
                CompradetallePK id = compradetalle.getCompradetallePK();
                if (findCompradetalle(id) == null) {
                    throw new NonexistentEntityException("The compradetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CompradetallePK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Compradetalle compradetalle;
            try {
                compradetalle = em.getReference(Compradetalle.class, id);
                compradetalle.getCompradetallePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compradetalle with id " + id + " no longer exists.", enfe);
            }
            Ordencompra ordencompra = compradetalle.getOrdencompra();
            if (ordencompra != null) {
                ordencompra.getCompradetalleCollection().remove(compradetalle);
                ordencompra = em.merge(ordencompra);
            }
            Producto producto = compradetalle.getProducto();
            if (producto != null) {
                producto.getCompradetalleCollection().remove(compradetalle);
                producto = em.merge(producto);
            }
            em.remove(compradetalle);
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

    public List<Compradetalle> findCompradetalleEntities() {
        return findCompradetalleEntities(true, -1, -1);
    }

    public List<Compradetalle> findCompradetalleEntities(int maxResults, int firstResult) {
        return findCompradetalleEntities(false, maxResults, firstResult);
    }

    private List<Compradetalle> findCompradetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compradetalle.class));
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

    public Compradetalle findCompradetalle(CompradetallePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compradetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompradetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compradetalle> rt = cq.from(Compradetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
