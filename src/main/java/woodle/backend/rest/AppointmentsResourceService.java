package woodle.backend.rest;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.UnkownMemberException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/appointments")
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

    @GET
    @Produces(value = "application/json")
    @Wrapped(element = "list", namespace = "http://foo.org", prefix = "foo")
    public AppointmentListing serviceGetAppointments() {
        return getListing();
    }

    private AppointmentListing getListing() {
        Set<Appointment> appointmentSet = new HashSet<Appointment>();
        appointmentSet.addAll(woodleStore.getAppointmentMap().values());
        AppointmentListing appointmentListing = new AppointmentListing(appointmentSet);
        System.out.println("returning list sized " + appointmentListing.getAppointments().size());
        return appointmentListing;
    }
}

