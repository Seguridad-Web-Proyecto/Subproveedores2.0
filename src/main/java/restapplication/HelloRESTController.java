/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restapplication;

/**
 *
 * @author jcami
 */

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("")
public class HelloRESTController {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {
        return Response.ok("Hola!. Esta es la ruta de servicios web!!!").build();
    }
    
}
