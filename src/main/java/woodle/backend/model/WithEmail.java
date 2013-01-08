package woodle.backend.model;

import com.google.common.base.Predicate;

import javax.annotation.Nullable;

public class WithEmail implements Predicate<ComparableAttendance> {
    final private String eMail;

    public WithEmail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public boolean apply(@Nullable ComparableAttendance o) {
        return o.getAttendantEmail().equalsIgnoreCase(eMail);
    }
}
