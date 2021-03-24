package org.corant.jms.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * corant <br>
 *
 * @auther sushuaihao 2020/11/16
 * @since
 */
@Path("/app")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldEndpoint {

  @Inject JMSContext jmsContext;

  @Path("/greeting")
  @GET
  public Response hello() {
    JMSProducer producer = jmsContext.createProducer();
    return Response.ok("Hello World!").build();
  }
}
