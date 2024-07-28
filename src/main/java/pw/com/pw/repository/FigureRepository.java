package pw.com.pw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.com.pw.domain.Figure;

public interface FigureRepository extends JpaRepository<Figure, String> {
    List<Figure> findByIsDeletedIsNull();

} 
