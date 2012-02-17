package woodle.backend.rest;

import woodle.backend.model.Appointment;
import woodle.backend.model.Attendance;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/appointments")
public interface AppointmentResource {

    @PUT
    @Consumes(value = "application/json")
    public Response.Status create(Appointment appointment);

    @PUT
    @Consumes(value = "application/json")
    @Path("/{appointmentId}/attendance")
    public String attend(@PathParam("appointmentId") String appointmentId, Attendance attendance);

    @DELETE
    @Consumes(value = "application/json")
    @Path("/{appointmentId}/attendance")
    public String cancel(@PathParam("appointmentId") String appointmentId, String creatorEmail);

    @DELETE
    @Consumes(value = "application/json")
    @Path("/{appointmentId}")
    public Response.Status deleteAppointment(@PathParam("appointmentId") String appointmentId, String creatorEmail);

    @GET
    @Produces(value = "application/json")
    public List<Appointment> clientGetAppointments();

    @GET
    @Produces(value = "application/json")
    @Path("/{appointmentId}")
    Appointment lookupById(@PathParam("appointmentId") String appointmentId);
}