package com.studymate.backend.zoom.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ZoomInterpreterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public String email;

    public String languages;

    @Override
    public String toString() {
        return "ZoomInterpreterDTO [email=" + getEmail() + ", languages=" + getLanguages() + "]";
    }

}