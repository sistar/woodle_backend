package woodle.backend.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Role {

    private String user_role;
    private String role_group;

    public Role() {
    }

    public Role(String role) {
        this.setUser_role(role);
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getRole_group() {
        return role_group;
    }

    public void setRole_group(String role_group) {
        this.role_group = role_group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (role_group != null ? !role_group.equals(role.role_group) : role.role_group != null) return false;
        return !(user_role != null ? !user_role.equals(role.user_role) : role.user_role != null);

    }

    @Override
    public int hashCode() {
        int result = 17 + (user_role != null ? user_role.hashCode() : 0);
        result = 31 * result + (role_group != null ? role_group.hashCode() : 0);
        return result;
    }
}
