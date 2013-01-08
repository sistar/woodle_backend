package woodle.backend.rest;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
public class AppointmentsResourceService implements AppointmentResource {

    @Context
    SecurityContext context;

    @Inject
    private WoodleStore woodleStore;

    @Inject
    Logger logger;

    @Override
    public Response.Status create(Appointment appointment) {
        logger.info("STORING " + appointment);
        try {
            woodleStore.saveAppointment(appointment);
        } catch (UnkownMemberException e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }

        return Response.Status.CREATED;
    }

    @Override
    @Deprecated
    public String attend(String creatorEmail,
                         String appointmentId,
                         Attendance attendance) {
        try {
            return woodleStore.attend(creatorEmail, appointmentId, attendance);
        } catch (UnkownAppointmentException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    public String attend(@PathParam("creatorEmail") String creatorEmail,
                         @PathParam("title") String title,
                         @PathParam("startDate") String startDate,
                         @QueryParam("calendarEventId") String calendarEventId) {
        try {
            return woodleStore.attend(creatorEmail, title, startDate, calendarEventId,
                    context.getUserPrincipal().getName());
        } catch (UnkownAppointmentException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    @Deprecated
    public String cancel(String appointmentId,
                         String creatorEmail) {
        try {
            return woodleStore.cancel(appointmentId, creatorEmail, context.getUserPrincipal().getName());
        } catch (UnkownAppointmentException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    public String cancel(@PathParam("creatorEmail") String creatorEmail,
                         @PathParam("title") String title,
                         @PathParam("startDate") String startDate) {
        try {
            return woodleStore.cancel(new AppointmentKey(title, startDate, creatorEmail),
                    context.getUserPrincipal().getName());
        } catch (UnkownAppointmentException e) {
            throw new WebApplicationException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    @Deprecated
    public Response.Status deleteAppointment(@PathParam("appointmentId") String appointmentId, String creatorEmail) {
        woodleStore.deleteAppointment(appointmentId, creatorEmail);
        return Response.Status.NO_CONTENT;
    }

    @Override
    public Response.Status deleteAppointment(@PathParam("creatorEmail") String creatorEmail,
                                             @PathParam("title") String title,
                                             @PathParam("startDate") String startDate) {
        woodleStore.deleteAppointment(new AppointmentKey(title, startDate, creatorEmail));
        return Response.Status.NO_CONTENT;
    }

    @Override
    public List<Appointment> clientGetAppointments() {
        return new ArrayList<Appointment>(woodleStore.getAppointmentMap().values());
    }

    @Override
    public Appointment lookupById(@PathParam("creatorEmail") String creatorEmail,
                                  @PathParam("title") String title,
                                  @PathParam("startDate") String startDate) {
        AppointmentKey appointmentKey = Appointment.createAppointmentKey(creatorEmail, title, startDate);

        Appointment appointment = woodleStore.getAppointmentMap().get(appointmentKey);
        if (appointment == null) {
            StringBuilder sb = new StringBuilder();

            sb.append(appointmentKey).append(" was not found");

            for (AppointmentKey key : woodleStore.getAppointmentMap().keySet()) {
                sb.append(key.toString());
                sb.append(" : ");
            }
            sb.append("are the known appointment keys");

            throw new WebApplicationException(new Throwable(sb.toString()), Response.Status.NOT_FOUND);
        } else {
            return appointment;
        }
    }
}

