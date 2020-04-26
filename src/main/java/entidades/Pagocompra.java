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
@Table(name = "pagocompra")
@NamedQueries({
    @NamedQuery(name = "Pagocompra.findAll", query = "SELECT p FROM Pagocompra p"),
    @NamedQuery(name = "Pagocompra.findByPagocompraid", query = "SELECT p FROM Pagocompra p WHERE p.pagocompraid = :pagocompraid"),
    @NamedQuery(name = "Pagocompra.findByFechaPago", query = "SELECT p FROM Pagocompra p WHERE p.fechaPago = :fechaPago"),
    @NamedQuery(name = "Pagocompra.findByMonto", query = "SELECT p FROM Pagocompra p WHERE p.monto = :monto"),
    @NamedQuery(name = "Pagocompra.findByEstado", query = "SELECT p FROM Pagocompra p WHERE p.estado = :estado")})
public class Pagocompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pagocompraid")
    private Long pagocompraid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private long monto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "tarjetacreditoid", referencedColumnName = "tarjetacreditocompraid")
    @ManyToOne(optional = false)
    private Tarjetacreditocompra tarjetacreditoid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pagoid")
    private Collection<Facturacompra> facturacompraCollection;

    public Pagocompra() {
    }

    public Pagocompra(Long pagocompraid) {
        this.pagocompraid = pagocompraid;
    }

    public Pagocompra(Long pagocompraid, Date fechaPago, long monto, String estado) {
        this.pagocompraid = pagocompraid;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.estado = estado;
    }

    public Long getPagocompraid() {
        return pagocompraid;
    }

    public void setPagocompraid(Long pagocompraid) {
        this.pagocompraid = pagocompraid;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public long getMonto() {
        return monto;
    }

    public void setMonto(long monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Tarjetacreditocompra getTarjetacreditoid() {
        return tarjetacreditoid;
    }

    public void setTarjetacreditoid(Tarjetacreditocompra tarjetacreditoid) {
        this.tarjetacreditoid = tarjetacreditoid;
    }

    public Collection<Facturacompra> getFacturacompraCollection() {
        return facturacompraCollection;
    }

    public void setFacturacompraCollection(Collection<Facturacompra> facturacompraCollection) {
        this.facturacompraCollection = facturacompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagocompraid != null ? pagocompraid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagocompra)) {
            return false;
        }
        Pagocompra other = (Pagocompra) object;
        if ((this.pagocompraid == null && other.pagocompraid != null) || (this.pagocompraid != null && !this.pagocompraid.equals(other.pagocompraid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Pagocompra[ pagocompraid=" + pagocompraid + " ]";
    }
    
}
