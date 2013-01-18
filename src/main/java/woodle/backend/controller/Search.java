package woodle.backend.controller;

import woodle.backend.data.WoodleStore;
import woodle.backend.model.Appointment;
import woodle.backend.model.Attendance;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Search {

    @Inject
    private WoodleStore woodleStore;

    @Produces
    @Named
    private List<Appointment> appointments = new ArrayList<Appointment>();

    @PostConstruct
    public void init() {
        Set<Attendance> attendances = new HashSet<Attendance>();
        Set<Attendance> maybeAttendances = new HashSet<Attendance>();

        appointments.add(new Appointment("12345", "meeting some", "wood", "meeting the three wiches", "2013-02-02T23:00:00.220", "2013-02-02T23:10:00.220", attendances, maybeAttendances, "the user", 4));
    }
}
