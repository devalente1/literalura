package com.alura.literalura.model;

public enum Idioma {
    SPANISH("es"),
    ENGLISH("en"),
    FRENCH("fr"),
    GERMAN("de"),
    TAGALOG("tl"),
    PORTUGUESE("pt"),
    ITALIAN("it"),
    DUTCH("nl");

    private String idioma;

    Idioma(String idioma) {
        this.idioma = idioma;
    }

    public static Idioma fromString(String text) {
        for (Idioma i : Idioma.values()) {
            if (i.idioma.equalsIgnoreCase(text)) {
                return i;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado el idioma: " + text);
    }
}
