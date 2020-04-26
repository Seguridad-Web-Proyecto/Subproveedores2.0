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
import entidades.Facturacompra;
import entidades.Proveedor;
import entidades.Compradetalle;
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
public class OrdencompraJpaController implements Serializable {

    public OrdencompraJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ordencompra ordencompra) throws RollbackFailureException, Exception {
        if (ordencompra.getCompradetalleCollection() == null) {
            ordencompra.setCompradetalleCollection(new ArrayList<Compradetalle>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Facturacompra facturaid = ordencompra.getFacturaid();
            if (facturaid != null) {
                facturaid = em.getReference(facturaid.getClass(), facturaid.getFacturacompraid());
                ordencompra.setFacturaid(facturaid);
            }
            Proveedor proveedorid = ordencompra.getProveedorid();
            if (proveedorid != null) {
                proveedorid = em.getReference(proveedorid.getClass(), proveedorid.getProveedorid());
                ordencompra.setProveedorid(proveedorid);
            }
            Collection<Compradetalle> attachedCompradetalleCollection = new ArrayList<Compradetalle>();
            for (Compradetalle compradetalleCollectionCompradetalleToAttach : ordencompra.getCompradetalleCollection()) {
                compradetalleCollectionCompradetalleToAttach = em.getReference(compradetalleCollectionCompradetalleToAttach.getClass(), compradetalleCollectionCompradetalleToAttach.getCompradetallePK());
                attachedCompradetalleCollection.add(compradetalleCollectionCompradetalleToAttach);
            }
            ordencompra.setCompradetalleCollection(attachedCompradetalleCollection);
            em.persist(ordencompra);
            if (facturaid != null) {
                facturaid.getOrdencompraCollection().add(ordencompra);
                facturaid = em.merge(facturaid);
            }
            if (proveedorid != null) {
                proveedorid.getOrdencompraCollection().add(ordencompra);
                proveedorid = em.merge(proveedorid);
            }
            for (Compradetalle compradetalleCollectionCompradetalle : ordencompra.getCompradetalleCollection()) {
                Ordencompra oldOrdencompraOfCompradetalleCollectionCompradetalle = compradetalleCollectionCompradetalle.getOrdencompra();
                compradetalleCollectionCompradetalle.setOrdencompra(ordencompra);
                compradetalleCollectionCompradetalle = em.merge(compradetalleCollectionCompradetalle);
                if (oldOrdencompraOfCompradetalleCollectionCompradetalle != null) {
                    oldOrdencompraOfCompradetalleCollectionCompradetalle.getCompradetalleCollection().remove(compradetalleCollectionCompradetalle);
                    oldOrdencompraOfCompradetalleCollectionCompradetalle = em.merge(oldOrdencompraOfCompradetalleCollectionCompradetalle);
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

    public void edit(Ordencompra ordencompra) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ordencompra persistentOrdencompra = em.find(Ordencompra.class, ordencompra.getOrdencompraid());
            Facturacompra facturaidOld = persistentOrdencompra.getFacturaid();
            Facturacompra facturaidNew = ordencompra.getFacturaid();
            Proveedor proveedoridOld = persistentOrdencompra.getProveedorid();
            Proveedor proveedoridNew = ordencompra.getProveedorid();
            Collection<Compradetalle> compradetalleCollectionOld = persistentOrdencompra.getCompradetalleCollection();
            Collection<Compradetalle> compradetalleCollectionNew = ordencompra.getCompradetalleCollection();
            List<String> illegalOrphanMessages = null;
            for (Compradetalle compradetalleCollectionOldCompradetalle : compradetalleCollectionOld) {
                if (!compradetalleCollectionNew.contains(compradetalleCollectionOldCompradetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compradetalle " + compradetalleCollectionOldCompradetalle + " since its ordencompra field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (facturaidNew != null) {
                facturaidNew = em.getReference(facturaidNew.getClass(), facturaidNew.getFacturacompraid());
                ordencompra.setFacturaid(facturaidNew);
            }
            if (proveedoridNew != null) {
                proveedoridNew = em.getReference(proveedoridNew.getClass(), proveedoridNew.getProveedorid());
                ordencompra.setProveedorid(proveedoridNew);
            }
            Collection<Compradetalle> attachedCompradetalleCollectionNew = new ArrayList<Compradetalle>();
            for (Compradetalle compradetalleCollectionNewCompradetalleToAttach : compradetalleCollectionNew) {
                compradetalleCollectionNewCompradetalleToAttach = em.getReference(compradetalleCollectionNewCompradetalleToAttach.getClass(), compradetalleCollectionNewCompradetalleToAttach.getCompradetallePK());
                attachedCompradetalleCollectionNew.add(compradetalleCollectionNewCompradetalleToAttach);
            }
            compradetalleCollectionNew = attachedCompradetalleCollectionNew;
            ordencompra.setCompradetalleCollection(compradetalleCollectionNew);
            ordencompra = em.merge(ordencompra);
            if (facturaidOld != null && !facturaidOld.equals(facturaidNew)) {
                facturaidOld.getOrdencompraCollection().remove(ordencompra);
                facturaidOld = em.merge(facturaidOld);
            }
            if (facturaidNew != null && !facturaidNew.equals(facturaidOld)) {
                facturaidNew.getOrdencompraCollection().add(ordencompra);
                facturaidNew = em.merge(facturaidNew);
            }
            if (proveedoridOld != null && !proveedoridOld.equals(proveedoridNew)) {
                proveedoridOld.getOrdencompraCollection().remove(ordencompra);
                proveedoridOld = em.merge(proveedoridOld);
            }
            if (proveedoridNew != null && !proveedoridNew.equals(proveedoridOld)) {
                proveedoridNew.getOrdencompraCollection().add(ordencompra);
                proveedoridNew = em.merge(proveedoridNew);
            }
            for (Compradetalle compradetalleCollectionNewCompradetalle : compradetalleCollectionNew) {
                if (!compradetalleCollectionOld.contains(compradetalleCollectionNewCompradetalle)) {
                    Ordencompra oldOrdencompraOfCompradetalleCollectionNewCompradetalle = compradetalleCollectionNewCompradetalle.getOrdencompra();
                    compradetalleCollectionNewCompradetalle.setOrdencompra(ordencompra);
                    compradetalleCollectionNewCompradetalle = em.merge(compradetalleCollectionNewCompradetalle);
                    if (oldOrdencompraOfCompradetalleCollectionNewCompradetalle != null && !oldOrdencompraOfCompradetalleCollectionNewCompradetalle.equals(ordencompra)) {
                        oldOrdencompraOfCompradetalleCollectionNewCompradetalle.getCompradetalleCollection().remove(compradetalleCollectionNewCompradetalle);
                        oldOrdencompraOfCompradetalleCollectionNewCompradetalle = em.merge(oldOrdencompraOfCompradetalleCollectionNewCompradetalle);
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
                Long id = ordencompra.getOrdencompraid();
                if (findOrdencompra(id) == null) {
                    throw new NonexistentEntityException("The ordencompra with id " + id + " no longer exists.");
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
            Ordencompra ordencompra;
            try {
                ordencompra = em.getReference(Ordencompra.class, id);
                ordencompra.getOrdencompraid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordencompra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Compradetalle> compradetalleCollectionOrphanCheck = ordencompra.getCompradetalleCollection();
            for (Compradetalle compradetalleCollectionOrphanCheckCompradetalle : compradetalleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ordencompra (" + ordencompra + ") cannot be destroyed since the Compradetalle " + compradetalleCollectionOrphanCheckCompradetalle + " in its compradetalleCollection field has a non-nullable ordencompra field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Facturacompra facturaid = ordencompra.getFacturaid();
            if (facturaid != null) {
                facturaid.getOrdencompraCollection().remove(ordencompra);
                facturaid = em.merge(facturaid);
            }
            Proveedor proveedorid = ordencompra.getProveedorid();
            if (proveedorid != null) {
                proveedorid.getOrdencompraCollection().remove(ordencompra);
                proveedorid = em.merge(proveedorid);
            }
            em.remove(ordencompra);
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

    public List<Ordencompra> findOrdencompraEntities() {
        return findOrdencompraEntities(true, -1, -1);
    }

    public List<Ordencompra> findOrdencompraEntities(int maxResults, int firstResult) {
        return findOrdencompraEntities(false, maxResults, firstResult);
    }

    private List<Ordencompra> findOrdencompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ordencompra.class));
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

    public Ordencompra findOrdencompra(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ordencompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdencompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ordencompra> rt = cq.from(Ordencompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
