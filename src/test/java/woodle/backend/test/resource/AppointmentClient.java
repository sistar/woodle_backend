package woodle.backend.test.resource;

import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/appointments")
@org.jboss.resteasy.annotations.providers.jaxb.IgnoreMediaTypes("application/*+json")
public interface AppointmentClient {

    @PUT
    @Produces(value = "application/json")
    @Consumes(value = "application/json")
    public Response.Status create( Appointment appointment);

    @GET
    @Produces(value = "application/json")
    @Consumes(value = "application/json")
    public AppointmentListing clientGetAppointments();
}