package br.com.rogerio.Musicplaylist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rogerio.Musicplaylist.entity.MusicEntity;

public interface MusicRepository extends JpaRepository<MusicEntity, Long>{
}
