package com.alura.literalura.service;

public interface IConvierteDatos {
        public <T> T obtenerDatos(String json, Class<T> clase);
}
