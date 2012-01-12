package woodle.backend.rest;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import woodle.backend.data.UnkownMemberException;
import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.AppointmentListing;
import woodle.backend.model.AppointmentWrapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/appointments")
@RequestScoped
public class AppointmentsResource {

    @Inject
    WoodleStore woodleStore;

    @PUT
    @Consumes(value = "application/json")
    public void create(AppointmentWrapper appointmentWrapper) {
        try {
            woodleStore.saveAppointment(appointmentWrapper.getAppointment());
        } catch (UnkownMemberException e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }
        System.out.println("stored " + appointmentWrapper.getAppointment() + " newSize:" + woodleStore.getAppointmentMap().size());
    }

    @GET
    @Produces(value = "application/json")
    @Wrapped(element="list", namespace="http://foo.org", prefix="foo")
    public AppointmentListing serviceGetAppointments() {
        return getListing();
    }

    private AppointmentListing getListing()
    {
        Set<Appointment> appointmentSet = new HashSet<Appointment>();
        appointmentSet.addAll(woodleStore.getAppointmentMap().values());
        AppointmentListing appointmentListing = new AppointmentListing(appointmentSet);
        System.out.println("returning list sized " + appointmentListing.getAppointments().size());
        return appointmentListing;
    }
}

