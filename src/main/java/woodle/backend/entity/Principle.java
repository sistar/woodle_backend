package woodle.backend.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRINCIPLES")
public class Principle {

    private String id;

    public Principle() {

    }

    public Principle(String email, String password) {
        this.id = email;
        this.password = password;
        Role role = new Role();
        role.setRole_group("Role");
        role.setUser_role("known");
        this.roles.add(role);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private Set<Role> roles = new HashSet<Role>();

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

    private String password;

    @ElementCollection
    @CollectionTable(
            name = "ROLES",
            joinColumns = @JoinColumn(name = "principal_id")
    )
    public Set<Role> getRoles() {
        return this.roles;
    }
}
