package woodle.backend.test.resource;

import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.AppointmentWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/appointments")
public interface AppointmentClient {

    @PUT
    @Produces(value = "application/json")
    @Consumes(value = "application/json")
    public Response.Status create( AppointmentWrapper appointmentWrapper);

    @GET
    @Produces(value = "application/json")
    @Consumes(value = "application/json")
    public AppointmentListing clientGetAppointments();
}