package br.com.rogerio.Musicplaylist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rogerio.Musicplaylist.entity.PlaylistEntity;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long>{

}
