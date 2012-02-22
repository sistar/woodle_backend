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
    @Path("/{creatorEmail}/{appointmentId}/attendance")
    @Deprecated
    public String attend(@PathParam("creatorEmail") String creatorEmail,
                         @PathParam("appointmentId") String appointmentId,
                         Attendance attendance);

    @PUT
    @Path("/{creatorEmail}/{title}/{startDate}")
    public String attend(@PathParam("creatorEmail") String creatorEmail,
                         @PathParam("title") String title,
                         @PathParam("startDate") String startDate,
                         @QueryParam("calendarEventId") String calendarEventId
    );

    @DELETE
    @Consumes(value = "application/json")
    @Path("/{appointmentId}/attendance")
    @Deprecated
    public String cancel(@PathParam("appointmentId") String appointmentId, String creatorEmail);

    @DELETE
    @Path("/{creatorEmail}/{title}/{startDate}/attendance")
    public String cancel(@PathParam("creatorEmail") String creatorEmail,
                         @PathParam("title") String title,
                         @PathParam("startDate") String startDate
    );


    @DELETE
    @Consumes(value = "application/json")
    @Path("/{appointmentId}")
    @Deprecated
    public Response.Status deleteAppointment(@PathParam("appointmentId") String appointmentId, String creatorEmail);

    @DELETE
    @Path("/{creatorEmail}/{title}/{startDate}")
    public Response.Status deleteAppointment(@PathParam("creatorEmail") String creatorEmail,
                                             @PathParam("title") String title,
                                             @PathParam("startDate") String startDate);


    @GET
    @Produces(value = "application/json")
    public List<Appointment> clientGetAppointments();

    @GET
    @Produces(value = "application/json")
    @Path("/{creatorEmail}/{title}/{startDate}")
    Appointment lookupById(@PathParam("creatorEmail") String creatorEmail,
                           @PathParam("title") String title,
                           @PathParam("startDate") String startDate
    );
}