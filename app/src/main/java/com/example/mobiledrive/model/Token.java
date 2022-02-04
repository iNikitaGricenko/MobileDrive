package com.example.mobiledrive.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Token implements Serializable {
    private final String accessToken;
    private final String refreshToken;
}
