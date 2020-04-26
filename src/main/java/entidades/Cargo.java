/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
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
@Table(name = "cargo")
@NamedQueries({
    @NamedQuery(name = "Cargo.findAll", query = "SELECT c FROM Cargo c"),
    @NamedQuery(name = "Cargo.findByCargoid", query = "SELECT c FROM Cargo c WHERE c.cargoid = :cargoid"),
    @NamedQuery(name = "Cargo.findByNombreCargo", query = "SELECT c FROM Cargo c WHERE c.nombreCargo = :nombreCargo"),
    @NamedQuery(name = "Cargo.findBySalarioMensual", query = "SELECT c FROM Cargo c WHERE c.salarioMensual = :salarioMensual")})
public class Cargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cargoid")
    private Long cargoid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre_cargo")
    private String nombreCargo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "salario_mensual")
    private int salarioMensual;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargoid")
    private Collection<Historialtrabajo> historialtrabajoCollection;

    public Cargo() {
    }

    public Cargo(Long cargoid) {
        this.cargoid = cargoid;
    }

    public Cargo(Long cargoid, String nombreCargo, int salarioMensual) {
        this.cargoid = cargoid;
        this.nombreCargo = nombreCargo;
        this.salarioMensual = salarioMensual;
    }

    public Long getCargoid() {
        return cargoid;
    }

    public void setCargoid(Long cargoid) {
        this.cargoid = cargoid;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public int getSalarioMensual() {
        return salarioMensual;
    }

    public void setSalarioMensual(int salarioMensual) {
        this.salarioMensual = salarioMensual;
    }

    public Collection<Historialtrabajo> getHistorialtrabajoCollection() {
        return historialtrabajoCollection;
    }

    public void setHistorialtrabajoCollection(Collection<Historialtrabajo> historialtrabajoCollection) {
        this.historialtrabajoCollection = historialtrabajoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cargoid != null ? cargoid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cargo)) {
            return false;
        }
        Cargo other = (Cargo) object;
        if ((this.cargoid == null && other.cargoid != null) || (this.cargoid != null && !this.cargoid.equals(other.cargoid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Cargo[ cargoid=" + cargoid + " ]";
    }
    
}
