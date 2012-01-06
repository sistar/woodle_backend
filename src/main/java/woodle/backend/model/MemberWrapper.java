package woodle.backend.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MemberWrapper {

    Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
