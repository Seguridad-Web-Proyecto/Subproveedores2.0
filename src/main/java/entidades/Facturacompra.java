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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jaker
 */
@Entity
@Table(name = "facturacompra")
@NamedQueries({
    @NamedQuery(name = "Facturacompra.findAll", query = "SELECT f FROM Facturacompra f"),
    @NamedQuery(name = "Facturacompra.findByFacturacompraid", query = "SELECT f FROM Facturacompra f WHERE f.facturacompraid = :facturacompraid"),
    @NamedQuery(name = "Facturacompra.findByFechaEmision", query = "SELECT f FROM Facturacompra f WHERE f.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "Facturacompra.findByFechaVencimientoPago", query = "SELECT f FROM Facturacompra f WHERE f.fechaVencimientoPago = :fechaVencimientoPago"),
    @NamedQuery(name = "Facturacompra.findByDescripcion", query = "SELECT f FROM Facturacompra f WHERE f.descripcion = :descripcion")})
public class Facturacompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "facturacompraid")
    private Long facturacompraid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_vencimiento_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoPago;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "pagoid", referencedColumnName = "pagocompraid")
    @ManyToOne(optional = false)
    private Pagocompra pagoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaid")
    private Collection<Ordencompra> ordencompraCollection;

    public Facturacompra() {
    }

    public Facturacompra(Long facturacompraid) {
        this.facturacompraid = facturacompraid;
    }

    public Facturacompra(Long facturacompraid, Date fechaEmision, Date fechaVencimientoPago, String descripcion) {
        this.facturacompraid = facturacompraid;
        this.fechaEmision = fechaEmision;
        this.fechaVencimientoPago = fechaVencimientoPago;
        this.descripcion = descripcion;
    }

    public Long getFacturacompraid() {
        return facturacompraid;
    }

    public void setFacturacompraid(Long facturacompraid) {
        this.facturacompraid = facturacompraid;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimientoPago() {
        return fechaVencimientoPago;
    }

    public void setFechaVencimientoPago(Date fechaVencimientoPago) {
        this.fechaVencimientoPago = fechaVencimientoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Pagocompra getPagoid() {
        return pagoid;
    }

    public void setPagoid(Pagocompra pagoid) {
        this.pagoid = pagoid;
    }

    public Collection<Ordencompra> getOrdencompraCollection() {
        return ordencompraCollection;
    }

    public void setOrdencompraCollection(Collection<Ordencompra> ordencompraCollection) {
        this.ordencompraCollection = ordencompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturacompraid != null ? facturacompraid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturacompra)) {
            return false;
        }
        Facturacompra other = (Facturacompra) object;
        if ((this.facturacompraid == null && other.facturacompraid != null) || (this.facturacompraid != null && !this.facturacompraid.equals(other.facturacompraid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Facturacompra[ facturacompraid=" + facturacompraid + " ]";
    }
    
}
