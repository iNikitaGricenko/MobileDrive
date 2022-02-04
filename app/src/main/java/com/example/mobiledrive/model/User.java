package com.example.mobiledrive.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User implements Serializable {
    private Long id;

    private String firstName;
    private String lastName;
    private String surname;

    private String city;
    private String phone;
    private String password;
    private Set<Role> roles = new HashSet<>();

}
