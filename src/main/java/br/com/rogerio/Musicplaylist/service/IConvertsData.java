package br.com.rogerio.Musicplaylist.service;

public interface IConvertsData {
    public <T> T GetData (String json, Class<T> classe);
}
