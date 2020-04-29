/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import entidades.Producto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jpa.controllers.GananciaJpaController;

/**
 *
 * @author jcami
 */
@Stateless
@Path("productos")
public class ProductoFacadeREST extends AbstractFacade<Producto> {

    private final GananciaJpaController gananciaJpaController = 
            new GananciaJpaController(super.getUserTransaction(), super.getEntityManagerFactory());
    
    @PersistenceContext(unitName = "com.mycompany_Subproveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public ProductoFacadeREST() {
        super(Producto.class);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Producto find(@PathParam("id") Long id) {
        Producto producto =super.find(id);
        int gananciaxproducto = ((int)producto.getGanancia().getPorcentaje())*(int)producto.getPrecioUnitario();
        gananciaxproducto /= 100;
        gananciaxproducto += (int)producto.getPrecioUnitario();
        return producto;
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Producto> findAll() {
        List<Producto> productoList = super.findAll();
        for(Producto producto: productoList){
            int gananciaxproducto = ((int)producto.getGanancia().getPorcentaje())*(int)producto.getPrecioUnitario();
            gananciaxproducto /= 100;
            gananciaxproducto += (int)producto.getPrecioUnitario();
            producto.setPrecioUnitario(gananciaxproducto);
        }
        return productoList;
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Producto> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
