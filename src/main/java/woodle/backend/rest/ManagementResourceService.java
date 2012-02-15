package woodle.backend.rest;


import woodle.backend.data.WoodleStore;

import javax.inject.Inject;


public class ManagementResourceService implements ManagementResource {

    @Inject
    private WoodleStore woodleStore;

    public void reset() {

        woodleStore.resetMembers();
    }
}
