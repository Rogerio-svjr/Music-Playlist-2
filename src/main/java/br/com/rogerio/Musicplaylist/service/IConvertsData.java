package br.com.rogerio.Musicplaylist.service;

public interface IConvertsData {
    public <T> T getData (String json, Class<T> classe);
}
