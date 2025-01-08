package com.contact.entites;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the details of a contact form submission.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    
    /**
     * Name of the person submitting the contact form.
     */
    private String name;
    
    /**
     * Email address of the person submitting the contact form.
     */
    private String email;
    
    /**
     * Message content submitted via the contact form.
     */
    private String message;
    
    /**
     * Subject of the contact form submission.
     */
    private String subject;
}