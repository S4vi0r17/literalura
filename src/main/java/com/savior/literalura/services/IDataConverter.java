package com.savior.literalura.services;

public interface IDataConverter {
    <T> T convertJsonTo(String json, Class<T> clazz);
}
