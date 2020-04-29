/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication.service;

import dao.exceptions.RollbackFailureException;
import entidades.Cliente;
import entidades.Ganancia;
import jpa.controllers.ClienteJpaController;
import jpa.controllers.GananciaJpaController;
import jpa.controllers.ProductoJpaController;
import jpa.controllers.VentadetalleJpaController;
import entidades.Ordenventa;
import entidades.Producto;
import entidades.Ventadetalle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.Response;
import jpa.controllers.OrdenventaJpaController;

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
    
    private VentadetalleJpaController ventaDetalleJpaController;
    private ClienteJpaController clienteJpaController = 
            new ClienteJpaController(super.getUserTransaction(), super.getEntityManagerFactory());
    private ProductoJpaController productoJpaController = 
            new ProductoJpaController(super.getUserTransaction(), super.getEntityManagerFactory());
    private OrdenventaJpaController ordenventaJpaController;
    
    public OrdenventaFacadeREST() {
        super(Ordenventa.class);
    }

    @POST
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Ordenventa entity) {
        Cliente cliente = clienteJpaController.findClienteByEmail(entity.getClienteid().getEmail());
        if(cliente==null){
            return Response.status(Response.Status.BAD_REQUEST).entity("El cliente ingresado no existe").build();
        }
        entity.setClienteid(cliente);

        Long subtotal = 0L;
        Collection<Ventadetalle> detalles = entity.getVentadetalleCollection();
        for(Ventadetalle ventaDetalle: detalles){
            Long productoId = ventaDetalle.getProducto().getProductoid();
            Producto producto = productoJpaController.findProducto(productoId);
            if(producto==null){
                return Response.status(Response.Status.BAD_REQUEST).entity("El producto "+productoId+" no existe").build();
            }
            int gananciaxproducto = ((int)producto.getGanancia().getPorcentaje())*(int)producto.getPrecioUnitario();
            gananciaxproducto /= 100;
            gananciaxproducto += (int)producto.getPrecioUnitario();
            
            ventaDetalle.setPrecioUnitario(gananciaxproducto);
            Long importe = (long)gananciaxproducto*ventaDetalle.getCantidad();
            ventaDetalle.setProducto(producto);
            ventaDetalle.setImporte(importe);
            
            subtotal += importe;
        }
        Long total = subtotal*16/100 + subtotal;
        entity.setSubtotal(subtotal);
        entity.setTotal(total);
        entity.setIva((short)16);
        entity.setFechaVenta(new Date());
        entity.setStatus("pedido pendiente...");
        entity.setVentadetalleCollection(null);
        Ordenventa ordenventaIngresado = null;
        
        //INGRESAR DATOS AL SISTEMA ORDEN VENTA
        ordenventaJpaController =
            new OrdenventaJpaController(super.getUserTransaction(), super.getEntityManagerFactory());
        try {
            ordenventaIngresado = ordenventaJpaController.create(entity);
        } catch (Exception ex) {
            Logger.getLogger(OrdenventaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
        if(ordenventaIngresado==null){
            return Response.serverError().build();
        }
        
        // INGRESAMOS LOS DETALLES DE LA VENTA AL SISTEMA
        ventaDetalleJpaController = 
                new VentadetalleJpaController(super.getUserTransaction(), super.getEntityManagerFactory());
        for(Ventadetalle ventadetalle: detalles){
            ventadetalle.setOrdenventa(ordenventaIngresado);
            try {
                ventaDetalleJpaController.create(ventadetalle);
            } catch (Exception ex) {
                Logger.getLogger(OrdenventaFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                return Response.serverError().build();
            }
        }
        
        return Response.ok().build();
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
