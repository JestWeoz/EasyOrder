package org.easyoder.Model.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
public class User extends AbstractEntity {
    private String username;
    private String password;
    private String email;

}
