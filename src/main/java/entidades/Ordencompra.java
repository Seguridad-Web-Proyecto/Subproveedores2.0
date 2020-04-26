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
@Table(name = "ordencompra")
@NamedQueries({
    @NamedQuery(name = "Ordencompra.findAll", query = "SELECT o FROM Ordencompra o"),
    @NamedQuery(name = "Ordencompra.findByOrdencompraid", query = "SELECT o FROM Ordencompra o WHERE o.ordencompraid = :ordencompraid"),
    @NamedQuery(name = "Ordencompra.findByFechaCompra", query = "SELECT o FROM Ordencompra o WHERE o.fechaCompra = :fechaCompra"),
    @NamedQuery(name = "Ordencompra.findByStatus", query = "SELECT o FROM Ordencompra o WHERE o.status = :status"),
    @NamedQuery(name = "Ordencompra.findByIva", query = "SELECT o FROM Ordencompra o WHERE o.iva = :iva"),
    @NamedQuery(name = "Ordencompra.findBySubtotal", query = "SELECT o FROM Ordencompra o WHERE o.subtotal = :subtotal"),
    @NamedQuery(name = "Ordencompra.findByTotal", query = "SELECT o FROM Ordencompra o WHERE o.total = :total"),
    @NamedQuery(name = "Ordencompra.findByDescripcion", query = "SELECT o FROM Ordencompra o WHERE o.descripcion = :descripcion")})
public class Ordencompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ordencompraid")
    private Long ordencompraid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_compra")
    @Temporal(TemporalType.DATE)
    private Date fechaCompra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iva")
    private short iva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal")
    private long subtotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private long total;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordencompra")
    private Collection<Compradetalle> compradetalleCollection;
    @JoinColumn(name = "facturaid", referencedColumnName = "facturacompraid")
    @ManyToOne(optional = false)
    private Facturacompra facturaid;
    @JoinColumn(name = "proveedorid", referencedColumnName = "proveedorid")
    @ManyToOne(optional = false)
    private Proveedor proveedorid;

    public Ordencompra() {
    }

    public Ordencompra(Long ordencompraid) {
        this.ordencompraid = ordencompraid;
    }

    public Ordencompra(Long ordencompraid, Date fechaCompra, String status, short iva, long subtotal, long total, String descripcion) {
        this.ordencompraid = ordencompraid;
        this.fechaCompra = fechaCompra;
        this.status = status;
        this.iva = iva;
        this.subtotal = subtotal;
        this.total = total;
        this.descripcion = descripcion;
    }

    public Long getOrdencompraid() {
        return ordencompraid;
    }

    public void setOrdencompraid(Long ordencompraid) {
        this.ordencompraid = ordencompraid;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public short getIva() {
        return iva;
    }

    public void setIva(short iva) {
        this.iva = iva;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(long subtotal) {
        this.subtotal = subtotal;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Collection<Compradetalle> getCompradetalleCollection() {
        return compradetalleCollection;
    }

    public void setCompradetalleCollection(Collection<Compradetalle> compradetalleCollection) {
        this.compradetalleCollection = compradetalleCollection;
    }

    public Facturacompra getFacturaid() {
        return facturaid;
    }

    public void setFacturaid(Facturacompra facturaid) {
        this.facturaid = facturaid;
    }

    public Proveedor getProveedorid() {
        return proveedorid;
    }

    public void setProveedorid(Proveedor proveedorid) {
        this.proveedorid = proveedorid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ordencompraid != null ? ordencompraid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ordencompra)) {
            return false;
        }
        Ordencompra other = (Ordencompra) object;
        if ((this.ordencompraid == null && other.ordencompraid != null) || (this.ordencompraid != null && !this.ordencompraid.equals(other.ordencompraid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Ordencompra[ ordencompraid=" + ordencompraid + " ]";
    }
    
}
