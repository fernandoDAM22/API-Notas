package com.notes.notes.controller;

import com.notes.notes.entity.Category;
import com.notes.notes.service.CategoryService;
import com.notes.notes.tools.JWTUtil;
import com.notes.notes.tools.Responses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("api/categories")
public class CategoryController {
    /**
     * Instancia del repositorio
     */
    @Autowired
    private CategoryService service;
    /**
     * Instancia de la clase JWTUtil para poder acceder a los metodos jwt
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Este metodo permite insertar una categoria
     * @param token es el token de autenticacion del usuario al que se la va a insertar la categoria
     * @param category es la categoria que se va a insertar
     * @return un ResponseEntity indicando que se ha insertado la categoria correctamente o que ha ocurrido algun error
     */
    @PostMapping("/insert")
    public ResponseEntity<String> insert(@RequestHeader(value = "token") String token, @RequestBody Category category){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long id = Long.valueOf(jwtUtil.getKey(token));
        if(service.getUserCategory(category.getName(),id) != null){
            return Responses.conflict("Ya existe una categoria con ese nombre");
        }
        category.setUserId(id);
        if(service.save(category) != null){
            return Responses.created("Categoria insertada correctamente");
        }
        return Responses.badRequest("Error al crear la categoria");
    }

    /**
     * Este metodo permite obtener todas las categorias de la base de datos
     * @return una lista con todas las categorias de la base de datos
     */
    @GetMapping("/")
    public ResponseEntity<List<Category>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Este metodo permite ontener todas las categorias de un usuario
     * @param token es el token de autenticacion del usuario
     * @param id es el id del usuario del que queremos obtener las categorias
     * @return una lista con todas las categorias del usuario, un error en caso de que algo falle
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Category>> getAll(@RequestHeader("token") String token, @PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("autentication","Token invalido").body(null);
        }
        if(Long.parseLong(jwtUtil.getKey(token)) != id){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error","Solo puedes acceder a tus categorias").body(null);
        }
        return ResponseEntity.ok(service.getAllByUser(id));
    }

    /**
     * Este metodo permite obtener una categoria por su nombre
     * @param token es el token de autenticacion del usuario que esta intentando obtener una categoria
     * @param name es el nombre de la categoria que queremos obtener
     * @return un ResponseEntity con la categoria obtenida, o null en caso de que no exista
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getByName(@RequestHeader("token") String token, @PathVariable String name){
        if(!jwtUtil.verify(token)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).header("autentication","Token invalido").body(null);
        }
        Category category = service.findByName(name);
        Long userId = Long.valueOf(jwtUtil.getKey(token));
        if(category == null || !Objects.equals(category.getUserId(), userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Error","No existe esa categoria").body(null);
        }
        return ResponseEntity.ok(category);
    }

    /**
     * Este metodo permite borrar una categoria
     * @param token es el token de autenticacion del usuario al que le queremos borrar la categoria
     * @param id es el id de la categoria que queremos borrar
     * @return un ResposeEntity indicando que se ha borrado correctamente la categoria o de que ha ocurrido algun error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@RequestHeader("token") String token, @PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        Category category = service.findById(id);
        //comprobamos que la categoria existe y que pertenece al usuario autenticado
        if(category == null || !Objects.equals(category.getUserId(), userId)){
            return Responses.notFound("No existe esa categoria");
        }
        if(service.delete(id) != null){
            return ResponseEntity.ok("Categoria borrada correctamente");
        }else{
            return Responses.badRequest("Error al borrar la categoria");
        }
    }

    /**
     * Este metodo permite modificar una categoria existente en la base de datos
     * @param token es el token de autenticacion del usuario que esta intentando mofificar la categoria
     * @param category es la categoria con los datos que vamos a modificar
     * @return un ResponseEntity indicando que se ha modificado correctamente la categoria o de que ha ocurrido algun error
     */
    @PutMapping("/")
    public ResponseEntity<String> update(@RequestHeader("token") String token, @RequestBody Category category){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        Category dbCategory = service.findById(category.getId());
        //comrprobamos que la categoria existe y que pertenece al usuario autenticado
        if(dbCategory == null || !Objects.equals(dbCategory.getUserId(), userId)){
            return Responses.notFound("No existe esa categoria");
        }
        if(service.update(category) != null){
            return ResponseEntity.ok("Categoria modificada correctamente");
        }else {
            return Responses.badRequest("Error al modificar la categoria");
        }
    }

    /**
     * Este metodo permite modificar el nombre de una categoria
     * @param token es el token de autenticacion del usuario que esta intentado modificar el nombre de la categoria
     * @param name es el nombre nuevo que le vamos a asignar a la categoria
     * @param id es el id de la categoria que queremos modificar
     * @return un ResponseEntity indicando que ha modificado correctamente el nombre de la categoria o de que ha ocurrido algun error
     */
    @PatchMapping("/update/name/{id}")
    public ResponseEntity<String> updateName(@RequestHeader("token") String token,@RequestBody String name,@PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        Category dbCategory = service.findById(id);
        //comprobamos que la categoria existe y que pertenece al usuario autenticado
        if(dbCategory == null || !Objects.equals(dbCategory.getUserId(), userId)){
            return Responses.notFound("No existe esa categoria");
        }
        dbCategory.setName(name);
        if(service.save(dbCategory) != null){
            return ResponseEntity.ok("Nombre modificado correctamente");
        }else{
            return Responses.badRequest("Error al modificar el nombre de la categoria");
        }
    }

    /**
     * Este metodo permite modificar la descripion de una categoria
     * @param token es el token de autenticacion del usuario que esta intentando modificar la descripcion de la categoria
     * @param description es la nueva descripcion que se le va a asignar a la categoria
     * @param id es el id de la categoria a la que le queremos modificar la descripcion
     * @return un ResponseEntity indicado que se ha modificado correctamente la descripcion o ha ocurrido algun error
     */
    @PatchMapping("/update/description/{id}")
    public ResponseEntity<String> updateDescription(@RequestHeader("token") String token,@RequestBody String description,@PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        Category dbCategory = service.findById(id);
        if(dbCategory == null || !Objects.equals(dbCategory.getUserId(), userId)){
            return Responses.notFound("No existe esa categoria");
        }
        dbCategory.setDescription(description);
        if(service.save(dbCategory) != null){
            return ResponseEntity.ok("descripcion modificada correctamente");
        }else{
            return Responses.badRequest("Error al modificar el nombre de la categoria");
        }
    }
}
