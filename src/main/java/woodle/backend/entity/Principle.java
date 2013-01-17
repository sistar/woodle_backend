package woodle.backend.entity;

import woodle.backend.model.Member;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRINCIPLES")
public class Principle {

    private String id;
    private Set<Role> roles = new HashSet<Role>();
    private String password;
    private boolean rememberMe;

    public Principle() {

    }

    public Principle(String email, String password) {
        this.id = email;
        this.password = password;
        addKnownRole();
    }

    public Principle(Member newMember) {
        this.id = newMember.getEmail();
        this.password = newMember.getPassword();
        addKnownRole();
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    private void addKnownRole() {
        Role role = new Role();
        role.setRole_group("Role");
        role.setUser_role("known");
        this.roles.add(role);
    }

    @Id
    @Column(name = "PRINCIPAL_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ElementCollection
    @CollectionTable(
            name = "ROLES",
            joinColumns = @JoinColumn(name = "principal_id")
    )
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Principle{" +
                "id='" + id + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
