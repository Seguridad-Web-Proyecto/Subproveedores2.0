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
@Table(name = "pagoventa")
@NamedQueries({
    @NamedQuery(name = "Pagoventa.findAll", query = "SELECT p FROM Pagoventa p"),
    @NamedQuery(name = "Pagoventa.findByPagoventaid", query = "SELECT p FROM Pagoventa p WHERE p.pagoventaid = :pagoventaid"),
    @NamedQuery(name = "Pagoventa.findByFechaPago", query = "SELECT p FROM Pagoventa p WHERE p.fechaPago = :fechaPago"),
    @NamedQuery(name = "Pagoventa.findByMonto", query = "SELECT p FROM Pagoventa p WHERE p.monto = :monto"),
    @NamedQuery(name = "Pagoventa.findByEstado", query = "SELECT p FROM Pagoventa p WHERE p.estado = :estado")})
@XmlRootElement
public class Pagoventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pagoventaid")
    private Long pagoventaid;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pagoid")
    private Collection<Facturaventa> facturaventaCollection;
    @JoinColumn(name = "tarjetacreditoid", referencedColumnName = "tarjetacreditoventaid")
    @ManyToOne(optional = false)
    private Tarjetacreditoventa tarjetacreditoid;

    public Pagoventa() {
    }

    public Pagoventa(Long pagoventaid) {
        this.pagoventaid = pagoventaid;
    }

    public Pagoventa(Long pagoventaid, Date fechaPago, long monto, String estado) {
        this.pagoventaid = pagoventaid;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.estado = estado;
    }

    public Long getPagoventaid() {
        return pagoventaid;
    }

    public void setPagoventaid(Long pagoventaid) {
        this.pagoventaid = pagoventaid;
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

    @XmlTransient
    @JsonbTransient
    public Collection<Facturaventa> getFacturaventaCollection() {
        return facturaventaCollection;
    }

    public void setFacturaventaCollection(Collection<Facturaventa> facturaventaCollection) {
        this.facturaventaCollection = facturaventaCollection;
    }

    public Tarjetacreditoventa getTarjetacreditoid() {
        return tarjetacreditoid;
    }

    public void setTarjetacreditoid(Tarjetacreditoventa tarjetacreditoid) {
        this.tarjetacreditoid = tarjetacreditoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagoventaid != null ? pagoventaid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagoventa)) {
            return false;
        }
        Pagoventa other = (Pagoventa) object;
        if ((this.pagoventaid == null && other.pagoventaid != null) || (this.pagoventaid != null && !this.pagoventaid.equals(other.pagoventaid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Pagoventa[ pagoventaid=" + pagoventaid + " ]";
    }
    
}
