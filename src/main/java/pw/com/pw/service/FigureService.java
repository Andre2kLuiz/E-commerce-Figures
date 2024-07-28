package pw.com.pw.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pw.com.pw.domain.Figure;
import pw.com.pw.repository.FigureRepository;

@Service
public class FigureService {

    private final FigureRepository repository;
    public FigureService(FigureRepository repository){
        this.repository = repository;
    }

    public Optional<Figure> findById(String id){
        return repository.findById(id);
    }

    public void delete(String id) {
        Figure figure = repository.findById(id).orElseThrow(() -> new RuntimeException("Figure not found"));
        figure.setIsDeleted(new Date());
        repository.save(figure);
    }

    public Figure update(Figure f){
       return repository.saveAndFlush(f);
    }

    public Figure create(Figure figure) {
        return repository.save(figure);
    }

    public List<Figure> findAll() {
        return repository.findByIsDeletedIsNull();
    }
}
