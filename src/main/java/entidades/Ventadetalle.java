/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jaker
 */
@Entity
@Table(name = "ventadetalle")
@NamedQueries({
    @NamedQuery(name = "Ventadetalle.findAll", query = "SELECT v FROM Ventadetalle v"),
    @NamedQuery(name = "Ventadetalle.findByVentaid", query = "SELECT v FROM Ventadetalle v WHERE v.ventadetallePK.ventaid = :ventaid"),
    @NamedQuery(name = "Ventadetalle.findByProductoid", query = "SELECT v FROM Ventadetalle v WHERE v.ventadetallePK.productoid = :productoid"),
    @NamedQuery(name = "Ventadetalle.findByCantidad", query = "SELECT v FROM Ventadetalle v WHERE v.cantidad = :cantidad"),
    @NamedQuery(name = "Ventadetalle.findByPrecioUnitario", query = "SELECT v FROM Ventadetalle v WHERE v.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "Ventadetalle.findByImporte", query = "SELECT v FROM Ventadetalle v WHERE v.importe = :importe")})
public class Ventadetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VentadetallePK ventadetallePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_unitario")
    private int precioUnitario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "importe")
    private long importe;
    @JoinColumn(name = "ventaid", referencedColumnName = "ordenventaid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ordenventa ordenventa;
    @JoinColumn(name = "productoid", referencedColumnName = "productoid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public Ventadetalle() {
    }

    public Ventadetalle(VentadetallePK ventadetallePK) {
        this.ventadetallePK = ventadetallePK;
    }

    public Ventadetalle(VentadetallePK ventadetallePK, int cantidad, int precioUnitario, long importe) {
        this.ventadetallePK = ventadetallePK;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.importe = importe;
    }

    public Ventadetalle(long ventaid, long productoid) {
        this.ventadetallePK = new VentadetallePK(ventaid, productoid);
    }

    public VentadetallePK getVentadetallePK() {
        return ventadetallePK;
    }

    public void setVentadetallePK(VentadetallePK ventadetallePK) {
        this.ventadetallePK = ventadetallePK;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(int precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public long getImporte() {
        return importe;
    }

    public void setImporte(long importe) {
        this.importe = importe;
    }

    public Ordenventa getOrdenventa() {
        return ordenventa;
    }

    public void setOrdenventa(Ordenventa ordenventa) {
        this.ordenventa = ordenventa;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ventadetallePK != null ? ventadetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ventadetalle)) {
            return false;
        }
        Ventadetalle other = (Ventadetalle) object;
        if ((this.ventadetallePK == null && other.ventadetallePK != null) || (this.ventadetallePK != null && !this.ventadetallePK.equals(other.ventadetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Ventadetalle[ ventadetallePK=" + ventadetallePK + " ]";
    }
    
}
