/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Empleado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Persona;
import entidades.Usuario;
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
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws IllegalOrphanException, RollbackFailureException, Exception {
        if (empleado.getHistorialtrabajoCollection() == null) {
            empleado.setHistorialtrabajoCollection(new ArrayList<Historialtrabajo>());
        }
        List<String> illegalOrphanMessages = null;
        Persona personaOrphanCheck = empleado.getPersona();
        if (personaOrphanCheck != null) {
            Empleado oldEmpleadoOfPersona = personaOrphanCheck.getEmpleado();
            if (oldEmpleadoOfPersona != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Persona " + personaOrphanCheck + " already has an item of type Empleado whose persona column cannot be null. Please make another selection for the persona field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Persona persona = empleado.getPersona();
            if (persona != null) {
                persona = em.getReference(persona.getClass(), persona.getPersonaid());
                empleado.setPersona(persona);
            }
            Usuario usuarioid = empleado.getUsuarioid();
            if (usuarioid != null) {
                usuarioid = em.getReference(usuarioid.getClass(), usuarioid.getUsuarioid());
                empleado.setUsuarioid(usuarioid);
            }
            Collection<Historialtrabajo> attachedHistorialtrabajoCollection = new ArrayList<Historialtrabajo>();
            for (Historialtrabajo historialtrabajoCollectionHistorialtrabajoToAttach : empleado.getHistorialtrabajoCollection()) {
                historialtrabajoCollectionHistorialtrabajoToAttach = em.getReference(historialtrabajoCollectionHistorialtrabajoToAttach.getClass(), historialtrabajoCollectionHistorialtrabajoToAttach.getHistorialid());
                attachedHistorialtrabajoCollection.add(historialtrabajoCollectionHistorialtrabajoToAttach);
            }
            empleado.setHistorialtrabajoCollection(attachedHistorialtrabajoCollection);
            em.persist(empleado);
            if (persona != null) {
                persona.setEmpleado(empleado);
                persona = em.merge(persona);
            }
            if (usuarioid != null) {
                usuarioid.getEmpleadoCollection().add(empleado);
                usuarioid = em.merge(usuarioid);
            }
            for (Historialtrabajo historialtrabajoCollectionHistorialtrabajo : empleado.getHistorialtrabajoCollection()) {
                Empleado oldEmpleadoidOfHistorialtrabajoCollectionHistorialtrabajo = historialtrabajoCollectionHistorialtrabajo.getEmpleadoid();
                historialtrabajoCollectionHistorialtrabajo.setEmpleadoid(empleado);
                historialtrabajoCollectionHistorialtrabajo = em.merge(historialtrabajoCollectionHistorialtrabajo);
                if (oldEmpleadoidOfHistorialtrabajoCollectionHistorialtrabajo != null) {
                    oldEmpleadoidOfHistorialtrabajoCollectionHistorialtrabajo.getHistorialtrabajoCollection().remove(historialtrabajoCollectionHistorialtrabajo);
                    oldEmpleadoidOfHistorialtrabajoCollectionHistorialtrabajo = em.merge(oldEmpleadoidOfHistorialtrabajoCollectionHistorialtrabajo);
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

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getEmpleadoid());
            Persona personaOld = persistentEmpleado.getPersona();
            Persona personaNew = empleado.getPersona();
            Usuario usuarioidOld = persistentEmpleado.getUsuarioid();
            Usuario usuarioidNew = empleado.getUsuarioid();
            Collection<Historialtrabajo> historialtrabajoCollectionOld = persistentEmpleado.getHistorialtrabajoCollection();
            Collection<Historialtrabajo> historialtrabajoCollectionNew = empleado.getHistorialtrabajoCollection();
            List<String> illegalOrphanMessages = null;
            if (personaNew != null && !personaNew.equals(personaOld)) {
                Empleado oldEmpleadoOfPersona = personaNew.getEmpleado();
                if (oldEmpleadoOfPersona != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Persona " + personaNew + " already has an item of type Empleado whose persona column cannot be null. Please make another selection for the persona field.");
                }
            }
            for (Historialtrabajo historialtrabajoCollectionOldHistorialtrabajo : historialtrabajoCollectionOld) {
                if (!historialtrabajoCollectionNew.contains(historialtrabajoCollectionOldHistorialtrabajo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Historialtrabajo " + historialtrabajoCollectionOldHistorialtrabajo + " since its empleadoid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personaNew != null) {
                personaNew = em.getReference(personaNew.getClass(), personaNew.getPersonaid());
                empleado.setPersona(personaNew);
            }
            if (usuarioidNew != null) {
                usuarioidNew = em.getReference(usuarioidNew.getClass(), usuarioidNew.getUsuarioid());
                empleado.setUsuarioid(usuarioidNew);
            }
            Collection<Historialtrabajo> attachedHistorialtrabajoCollectionNew = new ArrayList<Historialtrabajo>();
            for (Historialtrabajo historialtrabajoCollectionNewHistorialtrabajoToAttach : historialtrabajoCollectionNew) {
                historialtrabajoCollectionNewHistorialtrabajoToAttach = em.getReference(historialtrabajoCollectionNewHistorialtrabajoToAttach.getClass(), historialtrabajoCollectionNewHistorialtrabajoToAttach.getHistorialid());
                attachedHistorialtrabajoCollectionNew.add(historialtrabajoCollectionNewHistorialtrabajoToAttach);
            }
            historialtrabajoCollectionNew = attachedHistorialtrabajoCollectionNew;
            empleado.setHistorialtrabajoCollection(historialtrabajoCollectionNew);
            empleado = em.merge(empleado);
            if (personaOld != null && !personaOld.equals(personaNew)) {
                personaOld.setEmpleado(null);
                personaOld = em.merge(personaOld);
            }
            if (personaNew != null && !personaNew.equals(personaOld)) {
                personaNew.setEmpleado(empleado);
                personaNew = em.merge(personaNew);
            }
            if (usuarioidOld != null && !usuarioidOld.equals(usuarioidNew)) {
                usuarioidOld.getEmpleadoCollection().remove(empleado);
                usuarioidOld = em.merge(usuarioidOld);
            }
            if (usuarioidNew != null && !usuarioidNew.equals(usuarioidOld)) {
                usuarioidNew.getEmpleadoCollection().add(empleado);
                usuarioidNew = em.merge(usuarioidNew);
            }
            for (Historialtrabajo historialtrabajoCollectionNewHistorialtrabajo : historialtrabajoCollectionNew) {
                if (!historialtrabajoCollectionOld.contains(historialtrabajoCollectionNewHistorialtrabajo)) {
                    Empleado oldEmpleadoidOfHistorialtrabajoCollectionNewHistorialtrabajo = historialtrabajoCollectionNewHistorialtrabajo.getEmpleadoid();
                    historialtrabajoCollectionNewHistorialtrabajo.setEmpleadoid(empleado);
                    historialtrabajoCollectionNewHistorialtrabajo = em.merge(historialtrabajoCollectionNewHistorialtrabajo);
                    if (oldEmpleadoidOfHistorialtrabajoCollectionNewHistorialtrabajo != null && !oldEmpleadoidOfHistorialtrabajoCollectionNewHistorialtrabajo.equals(empleado)) {
                        oldEmpleadoidOfHistorialtrabajoCollectionNewHistorialtrabajo.getHistorialtrabajoCollection().remove(historialtrabajoCollectionNewHistorialtrabajo);
                        oldEmpleadoidOfHistorialtrabajoCollectionNewHistorialtrabajo = em.merge(oldEmpleadoidOfHistorialtrabajoCollectionNewHistorialtrabajo);
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
                Long id = empleado.getEmpleadoid();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getEmpleadoid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Historialtrabajo> historialtrabajoCollectionOrphanCheck = empleado.getHistorialtrabajoCollection();
            for (Historialtrabajo historialtrabajoCollectionOrphanCheckHistorialtrabajo : historialtrabajoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Historialtrabajo " + historialtrabajoCollectionOrphanCheckHistorialtrabajo + " in its historialtrabajoCollection field has a non-nullable empleadoid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona persona = empleado.getPersona();
            if (persona != null) {
                persona.setEmpleado(null);
                persona = em.merge(persona);
            }
            Usuario usuarioid = empleado.getUsuarioid();
            if (usuarioid != null) {
                usuarioid.getEmpleadoCollection().remove(empleado);
                usuarioid = em.merge(usuarioid);
            }
            em.remove(empleado);
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

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
