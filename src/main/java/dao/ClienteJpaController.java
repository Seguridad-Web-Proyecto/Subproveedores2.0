/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws RollbackFailureException, Exception {
        if (cliente.getOrdenventaCollection() == null) {
            cliente.setOrdenventaCollection(new ArrayList<Ordenventa>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Ordenventa> attachedOrdenventaCollection = new ArrayList<Ordenventa>();
            for (Ordenventa ordenventaCollectionOrdenventaToAttach : cliente.getOrdenventaCollection()) {
                ordenventaCollectionOrdenventaToAttach = em.getReference(ordenventaCollectionOrdenventaToAttach.getClass(), ordenventaCollectionOrdenventaToAttach.getOrdenventaid());
                attachedOrdenventaCollection.add(ordenventaCollectionOrdenventaToAttach);
            }
            cliente.setOrdenventaCollection(attachedOrdenventaCollection);
            em.persist(cliente);
            for (Ordenventa ordenventaCollectionOrdenventa : cliente.getOrdenventaCollection()) {
                Cliente oldClienteidOfOrdenventaCollectionOrdenventa = ordenventaCollectionOrdenventa.getClienteid();
                ordenventaCollectionOrdenventa.setClienteid(cliente);
                ordenventaCollectionOrdenventa = em.merge(ordenventaCollectionOrdenventa);
                if (oldClienteidOfOrdenventaCollectionOrdenventa != null) {
                    oldClienteidOfOrdenventaCollectionOrdenventa.getOrdenventaCollection().remove(ordenventaCollectionOrdenventa);
                    oldClienteidOfOrdenventaCollectionOrdenventa = em.merge(oldClienteidOfOrdenventaCollectionOrdenventa);
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

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getClienteid());
            Collection<Ordenventa> ordenventaCollectionOld = persistentCliente.getOrdenventaCollection();
            Collection<Ordenventa> ordenventaCollectionNew = cliente.getOrdenventaCollection();
            List<String> illegalOrphanMessages = null;
            for (Ordenventa ordenventaCollectionOldOrdenventa : ordenventaCollectionOld) {
                if (!ordenventaCollectionNew.contains(ordenventaCollectionOldOrdenventa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ordenventa " + ordenventaCollectionOldOrdenventa + " since its clienteid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Ordenventa> attachedOrdenventaCollectionNew = new ArrayList<Ordenventa>();
            for (Ordenventa ordenventaCollectionNewOrdenventaToAttach : ordenventaCollectionNew) {
                ordenventaCollectionNewOrdenventaToAttach = em.getReference(ordenventaCollectionNewOrdenventaToAttach.getClass(), ordenventaCollectionNewOrdenventaToAttach.getOrdenventaid());
                attachedOrdenventaCollectionNew.add(ordenventaCollectionNewOrdenventaToAttach);
            }
            ordenventaCollectionNew = attachedOrdenventaCollectionNew;
            cliente.setOrdenventaCollection(ordenventaCollectionNew);
            cliente = em.merge(cliente);
            for (Ordenventa ordenventaCollectionNewOrdenventa : ordenventaCollectionNew) {
                if (!ordenventaCollectionOld.contains(ordenventaCollectionNewOrdenventa)) {
                    Cliente oldClienteidOfOrdenventaCollectionNewOrdenventa = ordenventaCollectionNewOrdenventa.getClienteid();
                    ordenventaCollectionNewOrdenventa.setClienteid(cliente);
                    ordenventaCollectionNewOrdenventa = em.merge(ordenventaCollectionNewOrdenventa);
                    if (oldClienteidOfOrdenventaCollectionNewOrdenventa != null && !oldClienteidOfOrdenventaCollectionNewOrdenventa.equals(cliente)) {
                        oldClienteidOfOrdenventaCollectionNewOrdenventa.getOrdenventaCollection().remove(ordenventaCollectionNewOrdenventa);
                        oldClienteidOfOrdenventaCollectionNewOrdenventa = em.merge(oldClienteidOfOrdenventaCollectionNewOrdenventa);
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
                Long id = cliente.getClienteid();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getClienteid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ordenventa> ordenventaCollectionOrphanCheck = cliente.getOrdenventaCollection();
            for (Ordenventa ordenventaCollectionOrphanCheckOrdenventa : ordenventaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Ordenventa " + ordenventaCollectionOrphanCheckOrdenventa + " in its ordenventaCollection field has a non-nullable clienteid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
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

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
