/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "inventario")
@NamedQueries({
    @NamedQuery(name = "Inventario.findAll", query = "SELECT i FROM Inventario i"),
    @NamedQuery(name = "Inventario.findByInventarioid", query = "SELECT i FROM Inventario i WHERE i.inventarioid = :inventarioid"),
    @NamedQuery(name = "Inventario.findByExistencias", query = "SELECT i FROM Inventario i WHERE i.existencias = :existencias")})
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "inventarioid")
    private Long inventarioid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "existencias")
    private int existencias;
    @JoinColumn(name = "productoid", referencedColumnName = "productoid")
    @ManyToOne(optional = false)
    private Producto productoid;

    public Inventario() {
    }

    public Inventario(Long inventarioid) {
        this.inventarioid = inventarioid;
    }

    public Inventario(Long inventarioid, int existencias) {
        this.inventarioid = inventarioid;
        this.existencias = existencias;
    }

    public Long getInventarioid() {
        return inventarioid;
    }

    public void setInventarioid(Long inventarioid) {
        this.inventarioid = inventarioid;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public Producto getProductoid() {
        return productoid;
    }

    public void setProductoid(Producto productoid) {
        this.productoid = productoid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inventarioid != null ? inventarioid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.inventarioid == null && other.inventarioid != null) || (this.inventarioid != null && !this.inventarioid.equals(other.inventarioid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Inventario[ inventarioid=" + inventarioid + " ]";
    }
    
}
