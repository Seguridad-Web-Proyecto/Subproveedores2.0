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
@Table(name = "tarjetacreditocompra")
@NamedQueries({
    @NamedQuery(name = "Tarjetacreditocompra.findAll", query = "SELECT t FROM Tarjetacreditocompra t"),
    @NamedQuery(name = "Tarjetacreditocompra.findByTarjetacreditocompraid", query = "SELECT t FROM Tarjetacreditocompra t WHERE t.tarjetacreditocompraid = :tarjetacreditocompraid"),
    @NamedQuery(name = "Tarjetacreditocompra.findByNumero", query = "SELECT t FROM Tarjetacreditocompra t WHERE t.numero = :numero"),
    @NamedQuery(name = "Tarjetacreditocompra.findByCvc", query = "SELECT t FROM Tarjetacreditocompra t WHERE t.cvc = :cvc"),
    @NamedQuery(name = "Tarjetacreditocompra.findByNombreTitular", query = "SELECT t FROM Tarjetacreditocompra t WHERE t.nombreTitular = :nombreTitular"),
    @NamedQuery(name = "Tarjetacreditocompra.findByFechaExpiracion", query = "SELECT t FROM Tarjetacreditocompra t WHERE t.fechaExpiracion = :fechaExpiracion"),
    @NamedQuery(name = "Tarjetacreditocompra.findByLugarFacturacion", query = "SELECT t FROM Tarjetacreditocompra t WHERE t.lugarFacturacion = :lugarFacturacion")})
public class Tarjetacreditocompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tarjetacreditocompraid")
    private Long tarjetacreditocompraid;
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
    private Collection<Pagocompra> pagocompraCollection;

    public Tarjetacreditocompra() {
    }

    public Tarjetacreditocompra(Long tarjetacreditocompraid) {
        this.tarjetacreditocompraid = tarjetacreditocompraid;
    }

    public Tarjetacreditocompra(Long tarjetacreditocompraid, BigInteger numero, short cvc, String nombreTitular, String fechaExpiracion, String lugarFacturacion) {
        this.tarjetacreditocompraid = tarjetacreditocompraid;
        this.numero = numero;
        this.cvc = cvc;
        this.nombreTitular = nombreTitular;
        this.fechaExpiracion = fechaExpiracion;
        this.lugarFacturacion = lugarFacturacion;
    }

    public Long getTarjetacreditocompraid() {
        return tarjetacreditocompraid;
    }

    public void setTarjetacreditocompraid(Long tarjetacreditocompraid) {
        this.tarjetacreditocompraid = tarjetacreditocompraid;
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

    public Collection<Pagocompra> getPagocompraCollection() {
        return pagocompraCollection;
    }

    public void setPagocompraCollection(Collection<Pagocompra> pagocompraCollection) {
        this.pagocompraCollection = pagocompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarjetacreditocompraid != null ? tarjetacreditocompraid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjetacreditocompra)) {
            return false;
        }
        Tarjetacreditocompra other = (Tarjetacreditocompra) object;
        if ((this.tarjetacreditocompraid == null && other.tarjetacreditocompraid != null) || (this.tarjetacreditocompraid != null && !this.tarjetacreditocompraid.equals(other.tarjetacreditocompraid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Tarjetacreditocompra[ tarjetacreditocompraid=" + tarjetacreditocompraid + " ]";
    }
    
}
