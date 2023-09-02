package ar.edu.uade.tpoapi.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.uade.tpoapi.modelo.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Integer>{
    
    public Optional<Unidad> findById(Integer  id);
    
}
