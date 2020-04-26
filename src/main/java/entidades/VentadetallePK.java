/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jaker
 */
@Embeddable
public class VentadetallePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ventaid")
    private long ventaid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "productoid")
    private long productoid;

    public VentadetallePK() {
    }

    public VentadetallePK(long ventaid, long productoid) {
        this.ventaid = ventaid;
        this.productoid = productoid;
    }

    public long getVentaid() {
        return ventaid;
    }

    public void setVentaid(long ventaid) {
        this.ventaid = ventaid;
    }

    public long getProductoid() {
        return productoid;
    }

    public void setProductoid(long productoid) {
        this.productoid = productoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ventaid;
        hash += (int) productoid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VentadetallePK)) {
            return false;
        }
        VentadetallePK other = (VentadetallePK) object;
        if (this.ventaid != other.ventaid) {
            return false;
        }
        if (this.productoid != other.productoid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VentadetallePK[ ventaid=" + ventaid + ", productoid=" + productoid + " ]";
    }
    
}
