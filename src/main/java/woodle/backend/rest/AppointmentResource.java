package woodle.backend.rest;

import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/appointments")
public interface AppointmentResource {

    @PUT
    @Produces(value = "application/json")
    @Consumes(value = "application/json")
    public Response.Status create(Appointment appointment);

    @GET
    @Produces(value = "application/json")
    @Consumes(value = "application/json")
    public AppointmentListing clientGetAppointments();
}