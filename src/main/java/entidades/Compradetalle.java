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
@Table(name = "compradetalle")
@NamedQueries({
    @NamedQuery(name = "Compradetalle.findAll", query = "SELECT c FROM Compradetalle c"),
    @NamedQuery(name = "Compradetalle.findByCompraid", query = "SELECT c FROM Compradetalle c WHERE c.compradetallePK.compraid = :compraid"),
    @NamedQuery(name = "Compradetalle.findByProductoid", query = "SELECT c FROM Compradetalle c WHERE c.compradetallePK.productoid = :productoid"),
    @NamedQuery(name = "Compradetalle.findByCantidad", query = "SELECT c FROM Compradetalle c WHERE c.cantidad = :cantidad"),
    @NamedQuery(name = "Compradetalle.findByPrecioUnitario", query = "SELECT c FROM Compradetalle c WHERE c.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "Compradetalle.findByImporte", query = "SELECT c FROM Compradetalle c WHERE c.importe = :importe")})
public class Compradetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompradetallePK compradetallePK;
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
    @JoinColumn(name = "compraid", referencedColumnName = "ordencompraid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ordencompra ordencompra;
    @JoinColumn(name = "productoid", referencedColumnName = "productoid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto;

    public Compradetalle() {
    }

    public Compradetalle(CompradetallePK compradetallePK) {
        this.compradetallePK = compradetallePK;
    }

    public Compradetalle(CompradetallePK compradetallePK, int cantidad, int precioUnitario, long importe) {
        this.compradetallePK = compradetallePK;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.importe = importe;
    }

    public Compradetalle(long compraid, long productoid) {
        this.compradetallePK = new CompradetallePK(compraid, productoid);
    }

    public CompradetallePK getCompradetallePK() {
        return compradetallePK;
    }

    public void setCompradetallePK(CompradetallePK compradetallePK) {
        this.compradetallePK = compradetallePK;
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

    public Ordencompra getOrdencompra() {
        return ordencompra;
    }

    public void setOrdencompra(Ordencompra ordencompra) {
        this.ordencompra = ordencompra;
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
        hash += (compradetallePK != null ? compradetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compradetalle)) {
            return false;
        }
        Compradetalle other = (Compradetalle) object;
        if ((this.compradetallePK == null && other.compradetallePK != null) || (this.compradetallePK != null && !this.compradetallePK.equals(other.compradetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Compradetalle[ compradetallePK=" + compradetallePK + " ]";
    }
    
}
