package com.notes.notes.controller;

import com.google.gson.Gson;
import com.notes.notes.entity.User;
import com.notes.notes.service.UserService;
import com.notes.notes.tools.JWTUtil;
import com.notes.notes.tools.Responses;
import com.notes.notes.tools.Verify;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/users")
public class UserController {
    /**
     * Instancia del servicio
     */
    @Autowired
    private UserService service;
    /**
     * Instancia de la clase que nos permite hacer uso de JWT
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Este metodo nos permite registrar un usuario en la base de datos
     *
     * @param user es els usuario que vamos a registrar en la base de datos
     * @return un ResponseEntity indicando que se ha insertado el usuario correctamente
     * o que ha ocurrido algun error
     */
    @PostMapping("/record")
    public ResponseEntity<String> insert(@RequestBody User user) {
        if(!Verify.verifyName(user.getName())){
            return Responses.badRequest("El nombre solo puede contener numeros y letras");
        }
        if(!Verify.verifyEmail(user.getEmail())){
            return Responses.badRequest("El email no es correcto");
        }
        //comprobamos que no existe un usuario con ese nombre
        if (service.findByName(user.getName()) != null) {
            return Responses.conflict("Ya existe un usuario con ese nombre");
        }
        //comprobamos que no existe un usuario con ese email
        if (service.findByEmail(user.getEmail()) != null) {
            return Responses.conflict("Ya existe un usuario con ese email");
        }
        //ciframos la contrasena del usuario
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        char[] password = user.getPassword().toCharArray();
        String hash = argon2.hash(1, 1024, 1, password);
        //se la asignamos al usuario
        user.setPassword(hash);
        //intentamos insertamos el usuario y retornamos la respuesta correspondiente
        if (service.save(user) != null) {
            return Responses.created("Usuario registrado correctamente");
        } else {
            return Responses.badRequest("Error al registrar el usuario");
        }
    }

    /**
     * Este metodo permite que un usuario inicie sesion en el sistema
     * @param user es el objeto user con los datos que del usuario que esta intentando iniciar sesion
     * @return un ResponseEntity con el token jwt del usuario en caso de que el login sea correcto, o
     * con el error correspondiente en caso de que sea incorrecto
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        //Obtenemos el usuario que intenta hacer login
        User authUser = service.findByEmail(user.getEmail());
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        //comprobamos que el usuario obtenido no sea nulo
        if (authUser != null) {
            //comprobamos que su contrasena sea correcta
            if (argon2.verify(authUser.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(jwtUtil.create(String.valueOf(authUser.getId()), authUser.getEmail()));
            } else {
                return Responses.notFound("Contraseña incorrecta");
            }
        }
        return Responses.notFound("No existe ningun usuario con ese email");
    }

    /**
     * Este metodo permite obtener todos los usuarios de la base de datos
     * @return una lista con todos los usuarios de la base de datos
     */
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllUsers());
    }

    /**
     * Este metodo permite borrar un usuario
     * @param id es el id del usuario que queremos borrar
     * @return un ResponseEntity no content en caso de que se borre el usuario, o con un
     * mensaje de error en caso de que no se pueda borrar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        User user = service.delete(id);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un usuario con ese id");
        }else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    /**
     * Este metodo permite actualizar todos los datos del usuario
     * @param user es el objeto usuario con los datos que vamos a actualizar
     * @return un ResponseEntity indicado que se ha modificado correctamente el usuario o no
     */
    @PutMapping("/")
    public  ResponseEntity<String> update(@RequestBody User user){
        //comprobamos que el nombre sea correcto
        if(!Verify.verifyName(user.getName())){
            return Responses.badRequest("El nombre solo puede contener numeros y letras");
        }
        //comprobamos que el email sea correcto
        if(!Verify.verifyEmail(user.getEmail())){
            return Responses.badRequest("El email no es correcto");
        }
        //comprobamos que exista un usuario con ese id
        if(user.getId() == null){
            ResponseEntity.badRequest().body(null);
        }
        //obtenemos el usuario que tenemos guardado en la base de datos con ese id
        User dbUser = service.findById(user.getId());
        //comprobamos que el nombre no pertenezca a otro usuario
        if(service.findByName(user.getName()) != null && !user.getName().equals(dbUser.getName())){
            return Responses.conflict("Ya existe un usuario con ese nombre");
        }
        //comprobamos que el email no pertenezca a otro usuario
        if(service.findByEmail(user.getEmail()) != null && !user.getEmail().equals(dbUser.getEmail())){
            return Responses.conflict("Ya existe un usuario con ese email");
        }
        //ciframos la contrasena del usuario
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        char[] password = user.getPassword().toCharArray();
        String hash = argon2.hash(1, 1024, 1, password);
        //se la asignamos al usuario
        user.setPassword(hash);
        //Intentamos modificar el usuario y le asignamos el mensaje de error correspondiente
        if(service.update(user) != null){
            Gson gson = new Gson();
            return ResponseEntity.ok(gson.toJson(user));
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Este metodo permite actualizar el nombre de un usuario
     * @param id es el id del usuario al que queremos actualizar el nombre
     * @param name es el nuevo nombre que le vamos a asignar al usuario
     * @return un ResponseEntity indicado que se ha modificado el nombre correctamente o ha ocurrido algun error
     */
    @PatchMapping("/update/name/{id}")
    public ResponseEntity<String> updateName(@PathVariable Long id, @RequestBody String name){
        //comprobamos que el nombre sea correcto
        if(!Verify.verifyName(name)){
            return Responses.badRequest("El nombre solo puede contener numeros y letras");
        }
        User dbUser = service.findById(id);
        if(dbUser == null){
            return Responses.notFound("No existe un usuario con ese id");
        }
        if(service.findByName(name) != null && !name.equals(dbUser.getName())){
            return Responses.conflict("Ya existe un usuario con ese nombre");
        }
        dbUser.setName(name);
        service.save(dbUser);
        return ResponseEntity.ok("Nombre modificado correctamente");
    }
    /**
     * Este metodo permite actualizar el email de un usuario
     * @param id es el id del usuario al que queremos actualizar el email
     * @param email es el nuevo email que le vamos a asignar al usuario
     * @return un ResponseEntity indicado que se ha modificado el email correctamente o ha ocurrido algun error
     */
    @PatchMapping("/update/email/{id}")
    public ResponseEntity<String> updateEmail(@PathVariable Long id, @RequestBody String email){
        //comprobamos que el email sea correcto
        if(!Verify.verifyEmail(email)){
            return Responses.badRequest("El email no es correcto");
        }
        User dbUser = service.findById(id);
        if(dbUser == null){
            return Responses.notFound("No existe un usuario con ese id");
        }
        if(service.findByEmail(email) != null && !email.equals(dbUser.getEmail())){
            return Responses.conflict("Ya existe un usuario con ese email");
        }
        dbUser.setEmail(email);
        service.save(dbUser);
        return ResponseEntity.ok("email modificado correctamente");
    }

    /**
     * Este metodo permite actualizar la contrasena de un usuario
     * @param id es el id del usuario al que le queremos modificar la contrasena
     * @param password es la nueva contrasena que le vamos a asignar el usuario
     * @return un ResponseEntity indicando que se ha modificado correctamente la contrasena o ha ocurrido algun error
     */
    @PatchMapping("/update/password/{id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody String password){
        User dbUser = service.findById(id);
        if(dbUser == null){
            return Responses.notFound("No existe un usuario con ese id");
        }
        //ciframos la contrasena del usuario
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        char[] charPassword = password.toCharArray();
        String hash = argon2.hash(1, 1024, 1, charPassword);
        //se la asignamos al usuario
        dbUser.setPassword(hash);
        service.save(dbUser);
        return ResponseEntity.ok("password modificada correctamente");
    }

}
