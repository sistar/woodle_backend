package woodle.backend.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "PRINCIPLE")
public class Principle {

    private String id;

    public Principle() {

    }

    public Principle(String email, String password) {
        this.id = email;
        this.password = password;
        Role role = new Role();
        role.setRoleGroup("known");
        role.setUserRole("known");
        this.roles.add(role);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private Set<Role> roles;

    @Id
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
    public Set<Role> getRoles() {
        return this.roles;
    }
}
