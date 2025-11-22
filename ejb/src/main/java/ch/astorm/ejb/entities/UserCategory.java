
package ch.astorm.ejb.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="sysuser_category")
public class UserCategory {
    @Id
    private Long id;
    @Column
    private String userpk;
    @Column
    private String roleid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserpk() {
        return userpk;
    }

    public void setUserpk(String userpk) {
        this.userpk = userpk;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
}
