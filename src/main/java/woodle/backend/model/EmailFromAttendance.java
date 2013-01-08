package woodle.backend.model;

import com.google.common.base.Function;

import javax.annotation.Nullable;

public class EmailFromAttendance implements Function<Attendance, String> {
    @Override
    public String apply(@Nullable Attendance attendance) {
        return attendance.getAttendantEmail();
    }
}
