package com.login.entities;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class) // Enables auditing for createdAt and updatedAt fields.
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = { "phone" }) })
public class User {
    /*
     * Represents the User entity in the application.
     * Maps to the "user" table in the database.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Primary key with auto-incremented value.
    private int id;

    @NotBlank(message = "Please enter user name")
    // Ensures the name field is not blank and provides a validation message.
    private String name;

    @Email(message = "Please enter valid email")
    // Validates that the email field contains a valid email address.
    private String email;

    @NotBlank(message = "Please enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
        message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    // Validates password with minimum size and pattern rules for security.
    private String password;

    @Column(unique = true)
    @Size(min = 10, max = 10, message = "Phone must be at least 10 characters long")
    // Ensures the phone field has exactly 10 characters and is unique in the database.
    private String phone;

    private String role; // Represents the role of the user (e.g., ADMIN, USER).

    @CreatedDate
    // Automatically sets the date when the entity is created.
    private Date createdAt;

    @LastModifiedDate
    // Automatically updates the date when the entity is modified.
    private Date updatedAt;
}