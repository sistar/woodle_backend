package woodle.backend.rest;

import woodle.backend.model.Appointment;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/appointments")
public interface AppointmentResource {

    @PUT
    @Consumes(value = "application/json")
    public Response.Status create(Appointment appointment);

    @GET
    @Produces(value = "application/json")
    public List<Appointment> clientGetAppointments();

    @GET
    @Produces(value = "application/json")
    @Path("/{appointmentId}")
    Appointment lookupById(@PathParam("appointmentId") String appointmentId);
}