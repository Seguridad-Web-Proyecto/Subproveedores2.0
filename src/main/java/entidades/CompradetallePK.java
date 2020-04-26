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
public class CompradetallePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "compraid")
    private long compraid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "productoid")
    private long productoid;

    public CompradetallePK() {
    }

    public CompradetallePK(long compraid, long productoid) {
        this.compraid = compraid;
        this.productoid = productoid;
    }

    public long getCompraid() {
        return compraid;
    }

    public void setCompraid(long compraid) {
        this.compraid = compraid;
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
        hash += (int) compraid;
        hash += (int) productoid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompradetallePK)) {
            return false;
        }
        CompradetallePK other = (CompradetallePK) object;
        if (this.compraid != other.compraid) {
            return false;
        }
        if (this.productoid != other.productoid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.CompradetallePK[ compraid=" + compraid + ", productoid=" + productoid + " ]";
    }
    
}
