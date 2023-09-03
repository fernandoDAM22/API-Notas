package com.notes.notes.service.impl;

import com.notes.notes.entity.Category;
import com.notes.notes.repository.CategoryRepository;
import com.notes.notes.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository respository;

    /**
     * Este metodo permite obtener todas las notas de la base de datos
     * @return una lista con todas las notas de la base de datos
     */
    @Override
    public List<Category> getAll() {
        return respository.findAll();
    }

    /**
     * Este metodo permite obtener todas las categorias de un usuario
     * @param id es el id del usuario del que queremos obtener sus categorias
     * @return una lista con todas las categorias del usuario con el id indicado
     */
    @Override
    public List<Category> getAllByUser(Long id) {
        return respository.getAllByUser(id);
    }

    /**
     * Este metodo permite obtener una categoria con un determinado nombre de un usuario concreto
     * @param name es el nombre de la categoria que queremos obtener
     * @param id es el id del usuario del que queremos obtener la categoria
     * @return la categoria con ese nombre y de ese usuario si existe, null si no existe
     */
    @Override
    public Category getUserCategory(String name, Long id) {
        return respository.getUserCategory(name,id);
    }

    /**
     * Este metodo permite obtener una categoria por su id
     * @param id es el id de la categoria que queremos obtener
     * @return la categoria con ese id si existe, null si no existe
     */
    @Override
    public Category findById(Long id) {
        Optional<Category> category = respository.findById(id);
        return category.orElse(null);
    }

    /**
     * Este metodo permite obtener una nota por su nombre
     * @param name es el nombre de la nota que queremos obtener
     * @return la categoria con ese nombre si existe, null si no existe
     */
    public Category findByName(String name) {
        Optional<Category> category = respository.findByName(name);
        return category.orElse(null);
    }

    /**
     * Este metodo permite guardar una categoria en la base de datos
     * @param category es la categoria que queremos guardar en la base de datos
     * @return la categoria guardada en la base de datos
     */
    @Override
    public Category save(Category category) {
        return respository.save(category);
    }

    /**
     * Este metodo permite borrar una categoria de la base de datos
     * @param id es el id de la categoria que queremos borrar
     * @return la categoria borrada
     */

    @Override
    public Category delete(Long id) {
        Optional<Category> category = respository.findById(id);
        if(category.isEmpty()){
            return null;
        }
        respository.delete(category.get());
        return category.get();
    }

    /**
     * Este metodo permite actualizar una categoria de la base de datos
     * @param id es el id de la categoria que queremos actualizar
     * @param category es la categoria con los nuevos datos
     * @return la cateogoria modificada
     */

    @Override
    public Category update(Category category) {
        Optional<Category> optionalCategory = respository.findById(category.getId());
        if(optionalCategory.isEmpty()){
            return null;
        }
        Category newCategory = optionalCategory.get();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());
        respository.save(newCategory);
        return newCategory;
    }
}
