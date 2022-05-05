package com.bank.privatebnk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="tbl_message")
public class Message implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;



    @NotBlank(message="Please Provide not blank name")
    @NotNull(message="Please provide your name")
    @Size(min=1, max=15, message="Your name '${validatedValue}' must be between {min} and {max} chars long")
    @Column(length=15, nullable=false)
    private String name;



    @NotBlank(message="Please Provide not blank subject")
    @NotNull(message="Please provide your subject")
    @Size(min=5, max=20, message="Your subject '${validatedValue}' must be between {min} and {max} chars long")
    @Column(length=20, nullable=false)
    private String subject;



    @NotBlank(message="Please Provide not blank body")
    @NotNull(message="Please provide your message body")
    @Size(min=20, max=200, message="Your body '${validatedValue}' must be between {min} and {max} chars long")
    @Column(length=200, nullable=false)
    private String body;


    @Email
    @Column(length=100,nullable=false)
    private String email;


    //555-555-5555
    //(555) 555 5555
    //555.555.5555
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please provide valid phone number")
    @Column(length=14,nullable = false)
    private String phoneNumber;


}