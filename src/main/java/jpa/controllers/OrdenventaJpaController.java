/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cliente;
import entidades.Facturaventa;
import entidades.Ordenventa;
import entidades.Ventadetalle;
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
public class OrdenventaJpaController implements Serializable {

    public OrdenventaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Ordenventa create(Ordenventa ordenventa) throws RollbackFailureException, Exception {
        if (ordenventa.getVentadetalleCollection() == null) {
            ordenventa.setVentadetalleCollection(new ArrayList<Ventadetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            Cliente clienteid = ordenventa.getClienteid();
            if (clienteid != null) {
                clienteid = em.getReference(clienteid.getClass(), clienteid.getClienteid());
                ordenventa.setClienteid(clienteid);
            }
            Facturaventa facturaid = ordenventa.getFacturaid();
            if (facturaid != null) {
                facturaid = em.getReference(facturaid.getClass(), facturaid.getFacturaventaid());
                ordenventa.setFacturaid(facturaid);
            }
            Collection<Ventadetalle> attachedVentadetalleCollection = new ArrayList<Ventadetalle>();
            for (Ventadetalle ventadetalleCollectionVentadetalleToAttach : ordenventa.getVentadetalleCollection()) {
                ventadetalleCollectionVentadetalleToAttach = em.getReference(ventadetalleCollectionVentadetalleToAttach.getClass(), ventadetalleCollectionVentadetalleToAttach.getVentadetallePK());
                attachedVentadetalleCollection.add(ventadetalleCollectionVentadetalleToAttach);
            }
            ordenventa.setVentadetalleCollection(attachedVentadetalleCollection);
            
            em.persist(ordenventa);
            if (clienteid != null) {
                clienteid.getOrdenventaCollection().add(ordenventa);
                clienteid = em.merge(clienteid);
            }
            if (facturaid != null) {
                facturaid.getOrdenventaCollection().add(ordenventa);
                facturaid = em.merge(facturaid);
            }
            for (Ventadetalle ventadetalleCollectionVentadetalle : ordenventa.getVentadetalleCollection()) {
                Ordenventa oldOrdenventaOfVentadetalleCollectionVentadetalle = ventadetalleCollectionVentadetalle.getOrdenventa();
                ventadetalleCollectionVentadetalle.setOrdenventa(ordenventa);
                ventadetalleCollectionVentadetalle = em.merge(ventadetalleCollectionVentadetalle);
                if (oldOrdenventaOfVentadetalleCollectionVentadetalle != null) {
                    oldOrdenventaOfVentadetalleCollectionVentadetalle.getVentadetalleCollection().remove(ventadetalleCollectionVentadetalle);
                    oldOrdenventaOfVentadetalleCollectionVentadetalle = em.merge(oldOrdenventaOfVentadetalleCollectionVentadetalle);
                }
            }
            em.flush();
        } catch (Exception ex) {
            try {
                ex.printStackTrace();
                //utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return ordenventa;
    }

    public void edit(Ordenventa ordenventa) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ordenventa persistentOrdenventa = em.find(Ordenventa.class, ordenventa.getOrdenventaid());
            Cliente clienteidOld = persistentOrdenventa.getClienteid();
            Cliente clienteidNew = ordenventa.getClienteid();
            Facturaventa facturaidOld = persistentOrdenventa.getFacturaid();
            Facturaventa facturaidNew = ordenventa.getFacturaid();
            Collection<Ventadetalle> ventadetalleCollectionOld = persistentOrdenventa.getVentadetalleCollection();
            Collection<Ventadetalle> ventadetalleCollectionNew = ordenventa.getVentadetalleCollection();
            List<String> illegalOrphanMessages = null;
            for (Ventadetalle ventadetalleCollectionOldVentadetalle : ventadetalleCollectionOld) {
                if (!ventadetalleCollectionNew.contains(ventadetalleCollectionOldVentadetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventadetalle " + ventadetalleCollectionOldVentadetalle + " since its ordenventa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteidNew != null) {
                clienteidNew = em.getReference(clienteidNew.getClass(), clienteidNew.getClienteid());
                ordenventa.setClienteid(clienteidNew);
            }
            if (facturaidNew != null) {
                facturaidNew = em.getReference(facturaidNew.getClass(), facturaidNew.getFacturaventaid());
                ordenventa.setFacturaid(facturaidNew);
            }
            Collection<Ventadetalle> attachedVentadetalleCollectionNew = new ArrayList<Ventadetalle>();
            for (Ventadetalle ventadetalleCollectionNewVentadetalleToAttach : ventadetalleCollectionNew) {
                ventadetalleCollectionNewVentadetalleToAttach = em.getReference(ventadetalleCollectionNewVentadetalleToAttach.getClass(), ventadetalleCollectionNewVentadetalleToAttach.getVentadetallePK());
                attachedVentadetalleCollectionNew.add(ventadetalleCollectionNewVentadetalleToAttach);
            }
            ventadetalleCollectionNew = attachedVentadetalleCollectionNew;
            ordenventa.setVentadetalleCollection(ventadetalleCollectionNew);
            ordenventa = em.merge(ordenventa);
            if (clienteidOld != null && !clienteidOld.equals(clienteidNew)) {
                clienteidOld.getOrdenventaCollection().remove(ordenventa);
                clienteidOld = em.merge(clienteidOld);
            }
            if (clienteidNew != null && !clienteidNew.equals(clienteidOld)) {
                clienteidNew.getOrdenventaCollection().add(ordenventa);
                clienteidNew = em.merge(clienteidNew);
            }
            if (facturaidOld != null && !facturaidOld.equals(facturaidNew)) {
                facturaidOld.getOrdenventaCollection().remove(ordenventa);
                facturaidOld = em.merge(facturaidOld);
            }
            if (facturaidNew != null && !facturaidNew.equals(facturaidOld)) {
                facturaidNew.getOrdenventaCollection().add(ordenventa);
                facturaidNew = em.merge(facturaidNew);
            }
            for (Ventadetalle ventadetalleCollectionNewVentadetalle : ventadetalleCollectionNew) {
                if (!ventadetalleCollectionOld.contains(ventadetalleCollectionNewVentadetalle)) {
                    Ordenventa oldOrdenventaOfVentadetalleCollectionNewVentadetalle = ventadetalleCollectionNewVentadetalle.getOrdenventa();
                    ventadetalleCollectionNewVentadetalle.setOrdenventa(ordenventa);
                    ventadetalleCollectionNewVentadetalle = em.merge(ventadetalleCollectionNewVentadetalle);
                    if (oldOrdenventaOfVentadetalleCollectionNewVentadetalle != null && !oldOrdenventaOfVentadetalleCollectionNewVentadetalle.equals(ordenventa)) {
                        oldOrdenventaOfVentadetalleCollectionNewVentadetalle.getVentadetalleCollection().remove(ventadetalleCollectionNewVentadetalle);
                        oldOrdenventaOfVentadetalleCollectionNewVentadetalle = em.merge(oldOrdenventaOfVentadetalleCollectionNewVentadetalle);
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
                Long id = ordenventa.getOrdenventaid();
                if (findOrdenventa(id) == null) {
                    throw new NonexistentEntityException("The ordenventa with id " + id + " no longer exists.");
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
            Ordenventa ordenventa;
            try {
                ordenventa = em.getReference(Ordenventa.class, id);
                ordenventa.getOrdenventaid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordenventa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ventadetalle> ventadetalleCollectionOrphanCheck = ordenventa.getVentadetalleCollection();
            for (Ventadetalle ventadetalleCollectionOrphanCheckVentadetalle : ventadetalleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ordenventa (" + ordenventa + ") cannot be destroyed since the Ventadetalle " + ventadetalleCollectionOrphanCheckVentadetalle + " in its ventadetalleCollection field has a non-nullable ordenventa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente clienteid = ordenventa.getClienteid();
            if (clienteid != null) {
                clienteid.getOrdenventaCollection().remove(ordenventa);
                clienteid = em.merge(clienteid);
            }
            Facturaventa facturaid = ordenventa.getFacturaid();
            if (facturaid != null) {
                facturaid.getOrdenventaCollection().remove(ordenventa);
                facturaid = em.merge(facturaid);
            }
            em.remove(ordenventa);
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

    public List<Ordenventa> findOrdenventaEntities() {
        return findOrdenventaEntities(true, -1, -1);
    }

    public List<Ordenventa> findOrdenventaEntities(int maxResults, int firstResult) {
        return findOrdenventaEntities(false, maxResults, firstResult);
    }

    private List<Ordenventa> findOrdenventaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ordenventa.class));
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

    public Ordenventa findOrdenventa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ordenventa.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdenventaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ordenventa> rt = cq.from(Ordenventa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
