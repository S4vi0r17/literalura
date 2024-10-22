package com.savior.literalura.services;

public interface IDataConverter {
    <T> T convertData(String json, Class<T> clazz);
}
