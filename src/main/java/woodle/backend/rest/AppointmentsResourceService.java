package woodle.backend.rest;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentKey;
import woodle.backend.model.UnkownMemberException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
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
        try {
            woodleStore.saveAppointment(appointment);
        } catch (UnkownMemberException e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }

        return Response.Status.CREATED;
    }

    @Override
    public List<Appointment> clientGetAppointments() {
        return new ArrayList<Appointment>(woodleStore.getAppointmentMap().values());
    }

    @Override
    public Appointment lookupById(@PathParam("appointmentId") String clientAppointmentId) {

        AppointmentKey appointmentKey = Appointment.createAppointmentKey(context.getUserPrincipal().getName(), clientAppointmentId);

        Appointment appointment = woodleStore.getAppointmentMap().get(appointmentKey);
        if (appointment == null) {
            StringBuilder sb = new StringBuilder();

            sb.append(appointmentKey + " was not found");

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

