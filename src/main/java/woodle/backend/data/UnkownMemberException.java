package woodle.backend.data;

public class UnkownMemberException extends Exception {
    private String memberEmail;

    @Override
    public String getMessage() {
        return "unknown member with E-Mail: " + memberEmail;
    }

    public UnkownMemberException(String memberEmail) {

        this.memberEmail = memberEmail;
    }
}
