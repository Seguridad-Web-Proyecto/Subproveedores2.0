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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jaker
 */
@Entity
@Table(name = "usuariosw")
@NamedQueries({
    @NamedQuery(name = "Usuariosw.findAll", query = "SELECT u FROM Usuariosw u"),
    @NamedQuery(name = "Usuariosw.findByUsuarioid", query = "SELECT u FROM Usuariosw u WHERE u.usuarioid = :usuarioid"),
    @NamedQuery(name = "Usuariosw.findByUsuario", query = "SELECT u FROM Usuariosw u WHERE u.usuario = :usuario"),
    @NamedQuery(name = "Usuariosw.findByContrasenia", query = "SELECT u FROM Usuariosw u WHERE u.contrasenia = :contrasenia"),
    @NamedQuery(name = "Usuariosw.findByActivo", query = "SELECT u FROM Usuariosw u WHERE u.activo = :activo"),
    @NamedQuery(name = "Usuariosw.findByEmpresa", query = "SELECT u FROM Usuariosw u WHERE u.empresa = :empresa"),
    @NamedQuery(name = "Usuariosw.findByRfc", query = "SELECT u FROM Usuariosw u WHERE u.rfc = :rfc")})
@XmlRootElement
public class Usuariosw implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usuarioid")
    private Long usuarioid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "contrasenia")
    private String contrasenia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "empresa")
    private String empresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "rfc")
    private String rfc;

    public Usuariosw() {
    }

    public Usuariosw(Long usuarioid) {
        this.usuarioid = usuarioid;
    }

    public Usuariosw(Long usuarioid, String usuario, String contrasenia, boolean activo, String empresa, String rfc) {
        this.usuarioid = usuarioid;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.activo = activo;
        this.empresa = empresa;
        this.rfc = rfc;
    }

    public Long getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(Long usuarioid) {
        this.usuarioid = usuarioid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioid != null ? usuarioid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuariosw)) {
            return false;
        }
        Usuariosw other = (Usuariosw) object;
        if ((this.usuarioid == null && other.usuarioid != null) || (this.usuarioid != null && !this.usuarioid.equals(other.usuarioid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usuariosw[ usuarioid=" + usuarioid + " ]";
    }
    
}
