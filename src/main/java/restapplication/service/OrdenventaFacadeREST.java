/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import entidades.Ordenventa;
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

/**
 * El cliente está emitiendo una orden de compra o haciendo un pedido.
 * En éste caso él va a indicar qué productos necesita. Pero para nosotros esto sería siendo una orden de venta.
 * Ya que le estamos vendiendo productos al cliente.
 * @author jcami
 */
@Stateless
@Path("pedidos")
public class OrdenventaFacadeREST extends AbstractFacade<Ordenventa> {

    @PersistenceContext(unitName = "com.mycompany_Subproveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public OrdenventaFacadeREST() {
        super(Ordenventa.class);
    }

    @POST
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Ordenventa entity) {
        super.create(entity);
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Ordenventa find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ordenventa> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ordenventa> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
