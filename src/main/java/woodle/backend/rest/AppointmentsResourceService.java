package woodle.backend.rest;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentKey;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.UnkownMemberException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;


@RequestScoped
public class AppointmentsResourceService implements AppointmentResource {

    @Inject
    WoodleStore woodleStore;


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
    public AppointmentListing clientGetAppointments() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Appointment lookupById(@PathParam("appointmentId") String clientAppointmentId) {

        AppointmentKey appointmentKey = Appointment.createAppointmentKey("santa@claus.no", clientAppointmentId);

        Appointment appointment = woodleStore.getAppointmentMap().get(appointmentKey);
        if (appointment == null) {
            StringBuilder sb = new StringBuilder();
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

    @GET
    @Produces(value = "application/json")
    public AppointmentListing serviceGetAppointments() {
        return getListing();
    }

    private AppointmentListing getListing() {
        Set<Appointment> appointmentSet = new HashSet<Appointment>();
        appointmentSet.addAll(woodleStore.getAppointmentMap().values());
        AppointmentListing appointmentListing = new AppointmentListing(appointmentSet);
        return appointmentListing;
    }
}

