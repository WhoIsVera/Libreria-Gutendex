package com.Libreria.service;

public interface IConvertirDatos {
    <T> T obtenerSerealizacionDatos (String json, Class<T> clase);
}
