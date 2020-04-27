/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.json.bind.annotation.JsonbTransient;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jaker
 */
@Entity
@Table(name = "facturaventa")
@NamedQueries({
    @NamedQuery(name = "Facturaventa.findAll", query = "SELECT f FROM Facturaventa f"),
    @NamedQuery(name = "Facturaventa.findByFacturaventaid", query = "SELECT f FROM Facturaventa f WHERE f.facturaventaid = :facturaventaid"),
    @NamedQuery(name = "Facturaventa.findByFechaEmision", query = "SELECT f FROM Facturaventa f WHERE f.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "Facturaventa.findByFechaVencimientoPago", query = "SELECT f FROM Facturaventa f WHERE f.fechaVencimientoPago = :fechaVencimientoPago"),
    @NamedQuery(name = "Facturaventa.findByDescripcion", query = "SELECT f FROM Facturaventa f WHERE f.descripcion = :descripcion")})
@XmlRootElement
public class Facturaventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "facturaventaid")
    private Long facturaventaid;
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
    @JoinColumn(name = "pagoid", referencedColumnName = "pagoventaid")
    @ManyToOne(optional = false)
    private Pagoventa pagoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facturaid")
    private Collection<Ordenventa> ordenventaCollection;

    public Facturaventa() {
    }

    public Facturaventa(Long facturaventaid) {
        this.facturaventaid = facturaventaid;
    }

    public Facturaventa(Long facturaventaid, Date fechaEmision, Date fechaVencimientoPago, String descripcion) {
        this.facturaventaid = facturaventaid;
        this.fechaEmision = fechaEmision;
        this.fechaVencimientoPago = fechaVencimientoPago;
        this.descripcion = descripcion;
    }

    public Long getFacturaventaid() {
        return facturaventaid;
    }

    public void setFacturaventaid(Long facturaventaid) {
        this.facturaventaid = facturaventaid;
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

    public Pagoventa getPagoid() {
        return pagoid;
    }

    public void setPagoid(Pagoventa pagoid) {
        this.pagoid = pagoid;
    }

    @XmlTransient
    @JsonbTransient
    public Collection<Ordenventa> getOrdenventaCollection() {
        return ordenventaCollection;
    }

    public void setOrdenventaCollection(Collection<Ordenventa> ordenventaCollection) {
        this.ordenventaCollection = ordenventaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaventaid != null ? facturaventaid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturaventa)) {
            return false;
        }
        Facturaventa other = (Facturaventa) object;
        if ((this.facturaventaid == null && other.facturaventaid != null) || (this.facturaventaid != null && !this.facturaventaid.equals(other.facturaventaid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Facturaventa[ facturaventaid=" + facturaventaid + " ]";
    }
    
}
