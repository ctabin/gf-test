
package ch.astorm.ejb.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="sysuser")
public class User {
    @Id
    private String userpk;
    @Column
    private String name;
    @Column
    private String password;

    public String getUserpk() {
        return userpk;
    }

    public void setUserpk(String userpk) {
        this.userpk = userpk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
