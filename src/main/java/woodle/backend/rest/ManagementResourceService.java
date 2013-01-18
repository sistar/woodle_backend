package woodle.backend.rest;

import woodle.backend.data.MemberRepository;
import woodle.backend.data.WoodleStore;

import javax.inject.Inject;

public class ManagementResourceService implements ManagementResource {

    @Inject
    private WoodleStore woodleStore;

    @Inject
    private MemberRepository memberRepository;

    public void reset() {
        memberRepository.reset();
        woodleStore.resetMembers();
        woodleStore.resetAppointments();
    }
}
