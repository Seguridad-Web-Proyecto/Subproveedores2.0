/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jaker
 */
@Entity
@Table(name = "empleado")
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findByEmpleadoid", query = "SELECT e FROM Empleado e WHERE e.empleadoid = :empleadoid"),
    @NamedQuery(name = "Empleado.findByFechaVinculo", query = "SELECT e FROM Empleado e WHERE e.fechaVinculo = :fechaVinculo"),
    @NamedQuery(name = "Empleado.findByFechaRetiro", query = "SELECT e FROM Empleado e WHERE e.fechaRetiro = :fechaRetiro")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "empleadoid")
    private Long empleadoid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_vinculo")
    @Temporal(TemporalType.DATE)
    private Date fechaVinculo;
    @Column(name = "fecha_retiro")
    @Temporal(TemporalType.DATE)
    private Date fechaRetiro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleadoid")
    private Collection<Historialtrabajo> historialtrabajoCollection;
    @JoinColumn(name = "empleadoid", referencedColumnName = "personaid", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Persona persona;
    @JoinColumn(name = "usuarioid", referencedColumnName = "usuarioid")
    @ManyToOne(optional = false)
    private Usuario usuarioid;

    public Empleado() {
    }

    public Empleado(Long empleadoid) {
        this.empleadoid = empleadoid;
    }

    public Empleado(Long empleadoid, Date fechaVinculo) {
        this.empleadoid = empleadoid;
        this.fechaVinculo = fechaVinculo;
    }

    public Long getEmpleadoid() {
        return empleadoid;
    }

    public void setEmpleadoid(Long empleadoid) {
        this.empleadoid = empleadoid;
    }

    public Date getFechaVinculo() {
        return fechaVinculo;
    }

    public void setFechaVinculo(Date fechaVinculo) {
        this.fechaVinculo = fechaVinculo;
    }

    public Date getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public Collection<Historialtrabajo> getHistorialtrabajoCollection() {
        return historialtrabajoCollection;
    }

    public void setHistorialtrabajoCollection(Collection<Historialtrabajo> historialtrabajoCollection) {
        this.historialtrabajoCollection = historialtrabajoCollection;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(Usuario usuarioid) {
        this.usuarioid = usuarioid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleadoid != null ? empleadoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.empleadoid == null && other.empleadoid != null) || (this.empleadoid != null && !this.empleadoid.equals(other.empleadoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Empleado[ empleadoid=" + empleadoid + " ]";
    }
    
}
