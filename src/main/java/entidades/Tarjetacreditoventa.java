/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jaker
 */
@Entity
@Table(name = "tarjetacreditoventa")
@NamedQueries({
    @NamedQuery(name = "Tarjetacreditoventa.findAll", query = "SELECT t FROM Tarjetacreditoventa t"),
    @NamedQuery(name = "Tarjetacreditoventa.findByTarjetacreditoventaid", query = "SELECT t FROM Tarjetacreditoventa t WHERE t.tarjetacreditoventaid = :tarjetacreditoventaid"),
    @NamedQuery(name = "Tarjetacreditoventa.findByNumero", query = "SELECT t FROM Tarjetacreditoventa t WHERE t.numero = :numero"),
    @NamedQuery(name = "Tarjetacreditoventa.findByCvc", query = "SELECT t FROM Tarjetacreditoventa t WHERE t.cvc = :cvc"),
    @NamedQuery(name = "Tarjetacreditoventa.findByNombreTitular", query = "SELECT t FROM Tarjetacreditoventa t WHERE t.nombreTitular = :nombreTitular"),
    @NamedQuery(name = "Tarjetacreditoventa.findByFechaExpiracion", query = "SELECT t FROM Tarjetacreditoventa t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "Tarjetacreditoventa.findByLugarFacturacion", query = "SELECT t FROM Tarjetacreditoventa t WHERE t.lugarFacturacion = :lugarFacturacion")})
public class Tarjetacreditoventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tarjetacreditoventaid")
    private Long tarjetacreditoventaid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private BigInteger numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cvc")
    private short cvc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre_titular")
    private String nombreTitular;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "fecha_expiracion")
    private String fechaExpiracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "lugar_facturacion")
    private String lugarFacturacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarjetacreditoid")
    private Collection<Pagoventa> pagoventaCollection;

    public Tarjetacreditoventa() {
    }

    public Tarjetacreditoventa(Long tarjetacreditoventaid) {
        this.tarjetacreditoventaid = tarjetacreditoventaid;
    }

    public Tarjetacreditoventa(Long tarjetacreditoventaid, BigInteger numero, short cvc, String nombreTitular, String fechaExpiracion, String lugarFacturacion) {
        this.tarjetacreditoventaid = tarjetacreditoventaid;
        this.numero = numero;
        this.cvc = cvc;
        this.nombreTitular = nombreTitular;
        this.fechaExpiracion = fechaExpiracion;
        this.lugarFacturacion = lugarFacturacion;
    }

    public Long getTarjetacreditoventaid() {
        return tarjetacreditoventaid;
    }

    public void setTarjetacreditoventaid(Long tarjetacreditoventaid) {
        this.tarjetacreditoventaid = tarjetacreditoventaid;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public void setNumero(BigInteger numero) {
        this.numero = numero;
    }

    public short getCvc() {
        return cvc;
    }

    public void setCvc(short cvc) {
        this.cvc = cvc;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getLugarFacturacion() {
        return lugarFacturacion;
    }

    public void setLugarFacturacion(String lugarFacturacion) {
        this.lugarFacturacion = lugarFacturacion;
    }

    public Collection<Pagoventa> getPagoventaCollection() {
        return pagoventaCollection;
    }

    public void setPagoventaCollection(Collection<Pagoventa> pagoventaCollection) {
        this.pagoventaCollection = pagoventaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarjetacreditoventaid != null ? tarjetacreditoventaid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjetacreditoventa)) {
            return false;
        }
        Tarjetacreditoventa other = (Tarjetacreditoventa) object;
        if ((this.tarjetacreditoventaid == null && other.tarjetacreditoventaid != null) || (this.tarjetacreditoventaid != null && !this.tarjetacreditoventaid.equals(other.tarjetacreditoventaid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Tarjetacreditoventa[ tarjetacreditoventaid=" + tarjetacreditoventaid + " ]";
    }
    
}
