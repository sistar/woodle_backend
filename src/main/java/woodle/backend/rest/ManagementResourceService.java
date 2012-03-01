package woodle.backend.rest;


import woodle.backend.controller.MemberRegistration;
import woodle.backend.data.WoodleStore;

import javax.inject.Inject;


public class ManagementResourceService implements ManagementResource {

    @Inject
    private WoodleStore woodleStore;

    @Inject
    private MemberRegistration memberRegistration;

    public void reset() {
        memberRegistration.reset();
        woodleStore.resetMembers();
        woodleStore.resetAppointments();
    }
}
