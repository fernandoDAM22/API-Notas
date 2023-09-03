package com.notes.notes.service.impl;

import com.notes.notes.entity.Category;
import com.notes.notes.entity.Note;
import com.notes.notes.repository.CategoryRepository;
import com.notes.notes.repository.NoteRepository;
import com.notes.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotesServiceImpl implements NoteService {

    @Autowired
    private NoteRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Note> getAll() {
        return repository.findAll();
    }

    /**
     * Este metodo permite obtener todas las notas pertenecientes a una lista de categorias
     * @param categories es la lista de las categorias de las que queremos obtener las notas
     * @return una lista con todas las notas pertenecientes a las categorias indicadas
     */
    @Override
    public List<Note> getAllByCategories(List<Long> categories) {
        return repository.getAllByCategories(categories);
    }

    /**
     * Este metodo permite obtener una lista con todas las notas de una categoria
     * @param id es el id de la categoria de la que queremos obtener las notas
     * @return una lista con todas las notas de la categoria indicada
     */
    @Override
    public List<Note> getAllByCategory(Long id) {
        return repository.getNotesByCategory(id);
    }

    /**
     * Este metodo permite obtener todas las notas de un usuario
     * @param id es el id del usuario del que queremos obtener las notas
     * @return una lista con todas las notas del usuario
     */
    @Override
    public List<Note> getAllByUser(Long id) {
        return repository.getAllByUser(id);
    }

    /**
     * Este metodo permite obtener una nota por su id
     * @param id es el id de la nota que queremos obtener
     * @return la nota con el id indicado si existe, null si no existe
     */
    @Override
    public Note findById(Long id) {
        Optional<Note> note = repository.findById(id);
        return note.orElse(null);
    }

    /**
     * Este metodo permite obtener una nota por su titulo
     * @param title es el titulo de la nota que queremos obtener
     * @return la nota con el titulo indicado si existe, null si no existe
     */
    @Override
    public Note findByTitle(String title) {
        Optional<Note> note = repository.findByTitle(title);
        return note.orElse(null);
    }

    /**
     * Este metodo permite guardar una nota en la base de datos
     * @param note es la nota que queremos  guardar en la base de datos
     * @return la nota guardada en la base de datos
     */
    @Override
    public Note save(Note note) {
        return repository.save(note);
    }

    /**
     * Este metodo permite borrar una nota de la base de datos
     * @param id es el id de la nota que queremos borrar
     * @return la nota borrada si existe, null si no existe
     */
    @Override
    public Note delete(Long id) {
        Optional<Note> optionalNote = repository.findById(id);
        if(optionalNote.isEmpty()){
            return null;
        }
        repository.delete(optionalNote.get());
        return optionalNote.get();
    }

    /**
     * Este metodo permite guardar una nota en la base da datos
     * @param note es la objeto nota con los nuevos datos de la nota
     * @return la nota actualizada
     */
    @Override
    public Note update(Note note) {
        Optional<Note> optionalNote = repository.findById(note.getId());
        if(optionalNote.isEmpty()){
            return null;
        }
        Note newNote = optionalNote.get();
        newNote.setTitle(note.getTitle());
        newNote.setContent(note.getContent());
        repository.save(newNote);
        return newNote;
    }

    /**
     * Esta metodo permite obtener la categoria de una nota
     * @param id es el id de la nota de la que queremos obtener su categoria
     * @return la categoria de la nota con ese id
     */
    @Override
    public Category getCategory(Long id) {
        Optional<Note> optionalNote = repository.findById(id);
        if(optionalNote.isEmpty()){
            return null;
        }
        Note note = optionalNote.get();
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            return null;
        }
        return category.get();
    }

    /**
     * Este metodo permite verificar que un usuario ya contiene una nota con un determinado titulo
     * @param title es el titulo de la nota que queremos verificar
     * @param id es el id del usuario al que le queremos verificar la nota
     * @return true si el usuario ya tiene una nota con ese titulo
     */
    @Override
    public boolean verify(String title, Long id) {
        Optional<Note> note = repository.verifyNote(title,id);
        return note.isPresent();
    }

}
