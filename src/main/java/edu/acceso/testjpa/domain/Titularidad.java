package edu.acceso.testjpa.domain;

import java.util.Arrays;

public enum Titularidad {

    PUBLICA("Pública"),
    PRIVADA("Privada");

    private String desc;

    Titularidad(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }

    public static Titularidad fromString(String desc) {
        return Arrays.stream(values())
            .filter(t -> t.desc.equalsIgnoreCase(desc))
            .findFirst()
            .orElse(null);
    }
}