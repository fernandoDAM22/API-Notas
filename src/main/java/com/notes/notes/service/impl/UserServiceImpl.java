package com.notes.notes.service.impl;

import com.notes.notes.entity.Note;
import com.notes.notes.entity.User;
import com.notes.notes.repository.UserRespository;
import com.notes.notes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    /**
     * instancia del repositorio
     */
    @Autowired
    private UserRespository respository;

    @Override
    public List<User> getAllUsers() {
        return respository.findAll();
    }

    /**
     * Este metodo permite obtener un usuario por su id
     * @param id es el id del usuario que queremos obtener
     * @return el usuario con ese id si existe, null si no existe
     */
    @Override
    public User findById(Long id) {
        Optional<User> user = respository.findById(id);
        return user.orElse(null);
    }

    /**
     * Este metodo permite obtener un usuario por su nombre
     * @param name es el nombre del usuario
     * @return el usuario con ese nombre si existe, null si no existe
     */
    @Override
    public User findByName(String name) {
        Optional<User> user = respository.findByName(name);
        return user.orElse(null);
    }

    /**
     * Este metodo permite obtener un usuario por su email
     * @param email es el email del usuario que queremos obtener
     * @return el usuario con ese email si existe, null si no
     */
    @Override
    public User findByEmail(String email) {
        Optional<User> user = respository.findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Este metodo permite guardar un usuario en la base de datos
     * @param user es el usuario que queremos guardar en la base de datos
     * @return el usuario guardado en la base de datos
     */
    @Override
    public User save(User user) {
        return respository.save(user);
    }

    /**
     * Este metodo permite borrar un usuario de la base de datos
     * @param id es el id del usuario que queremos borrar
     * @return el usuario borrado de la base de datos, null si no existe un usuario con el id indicado
     */
    @Override
    public User delete(Long id) {
        Optional<User> user = respository.findById(id);
        if(user.isEmpty()){
            return null;
        }
        respository.delete(user.get());
        return user.get();
    }

    /**
     * Este metodo permite actualizar un usuario
     * @param user es el objeto usuario con los nuevos datos del usuario a actualizar
     * @return el usuario modificado, null si ocurre algun error
     */
    @Override
    public User update(User user) {
        Optional<User> optionalUser = respository.findById(user.getId());
        if(optionalUser.isEmpty()){
            return null;
        }
        User newUser = optionalUser.get();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        respository.save(newUser);
        return newUser;
    }
    /**
     * Este metodo permite obtener todas las notas de un usuario
     * @param id es el id del usuario del que queremos obtener todas sus notas
     * @return una lista con todas las notas del usuario con el id indicados
     */
    @Override
    public List<Note> getAllNotes(Long id) {
        return respository.getAllNotes(id);
    }
}
