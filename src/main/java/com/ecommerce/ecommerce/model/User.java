package com.ecommerce.ecommerce.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true) // candidate key or alternate key
    @NotEmpty
    @Email(message = "{errors.invalidemail}")
    private String email;

    @NotEmpty
    private String password;
    
    // cascade- if we delete any user and that user has placed orders
    //  no matter how many then this cascade field dletes all the orders related to that users
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER) 
    // joinTable- on the basis of primary key of one table and foreign key of other table
    // merge two tables
    @JoinTable(
        name = "user_role",
        joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
    )
    private List<Role> roles;

    public User(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getEmail();
        this.roles = user.getRoles();
    }
    public User(){
        
    }
    
}
