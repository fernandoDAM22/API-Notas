package com.notes.notes.controller;

import com.notes.notes.entity.Category;
import com.notes.notes.entity.Note;
import com.notes.notes.service.CategoryService;
import com.notes.notes.service.NoteService;
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
@RequestMapping("api/notes")
public class NotesController {
    /**
     * Instancia del servicio para poder acceder a la base de datos
     */
    @Autowired
    private NoteService service;
    /**
     * Instancia del servicio de las categorias para poder accedera a ellas
     */
    @Autowired
    private CategoryService categoryService;
    /**
     * Instancia de JWTUtil para poder acceder al sistema jwt
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Este metodo permite insertar una nota en la base de datos
     * @param token es el token de autenticacion del usuario que esta intentando insertar una nota
     * @param note es la nota que se va a insertar
     * @return un ResponseEntity indicando que se ha insertado correctamente la nota o de que ha ocurrido algun error
     */
    @PostMapping("/")
    public ResponseEntity<String> insert(@RequestHeader("token") String token, @RequestBody Note note){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long id = Long.parseLong(jwtUtil.getKey(token));
        Category category = categoryService.findById(note.getCategoryId());
        note.setUserId(id);
        if(!Objects.equals(category.getUserId(), id)){
            return Responses.notFound("No existe esa categoria");
        }
        if(service.verify(note.getTitle(),id)){
            return Responses.conflict("Ya existe una categoria con ese titulo");
        }
        if(service.save(note) != null){
            return ResponseEntity.ok("Nota insertada correctamente");
        }
        return Responses.badRequest("Error al insertar la nota");
    }

    /**
     * Este metodo permite obtener todas las notas de la base de datos
     * @return un ResposeEntity con una lista con todas las notas de la base de datos
     */
    @GetMapping("/")
    public ResponseEntity<List<Note>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Este metodo permite obtener todas las notas de un usuario de la base de datos
     * @param token es el token de autenticacion del usuario que esta intentando obtener sus notas
     * @param id es el id del usuario del que queremos obtener todas sus notas
     * @return un ResponseEntity con las notas del usuario o un error en caso de que ocurra
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Note>> getAll(@RequestHeader("token") String token, @PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header("Error","Token de autenticacion invalido")
                    .body(null);
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        if(!userId.equals(id)){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Error","Solo puedes acceder a tus notas")
                    .body(null);
        }
        return ResponseEntity.ok(service.getAllByUser(id));
    }

    /**
     * Este metodo permite obtener todas las notas de una categoria
     * @param token es el token de autenticacion del usuario que quiere obtener las notas
     * @param id es el id de la categoria de la que queremos obtener las notas
     * @return una lista con las notas de esa categoria
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<List<Note>> getAllByCategory(@RequestHeader("token") String token,@PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header("Error","Token de autenticacion invalido")
                    .body(null);
        }
        Category category = categoryService.findById(id);
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        if(category == null || !Objects.equals(category.getUserId(), userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Error","No existe una categoria con ese id")
                    .body(null);
        }
        return ResponseEntity.ok(service.getAllByCategory(id));
    }

    /**
     * Este metodo permite obtener una lista con todas las notas de las categorias indicadas en la lista
     * @param token es el token de autenticacion del usuario
     * @param ids es la lista con los ids de las categorias de las que queremos obtener las notas
     * @return un ResponseEntity con una lista de las notas de las categorias indicadas, o un error si ocurre
     */
    @GetMapping("/categories/{ids}")
    public ResponseEntity<List<Note>> getAllByCategories(@RequestHeader("token") String token,@PathVariable List<Long> ids){
        if(!jwtUtil.verify(token)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .header("Error","Token de autenticacion invalido")
                    .body(null);
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        //verificamos que todas las categorias pertenezcan al usuario autenticado
        for (Long id : ids) {
            Category category = categoryService.findById(id);
            if(category == null || !Objects.equals(category.getUserId(), userId)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Error","No existe una categoria con el id " + id)
                        .body(null);
            }
        }
        return ResponseEntity.ok(service.getAllByCategories(ids));
    }

    /**
     * Este metodo nos permite eliminar una nota
     * @param token es el token de autenticacion del usuario que quiere eliminar la nota
     * @param id es el id de la nota que queremos borrar
     * @return un ResponseEntity indicando que se ha borrado la nota correctamente o que a ocurrido algun error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@RequestHeader("token") String token, @PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        Note note = service.findById(id);
        if(note == null || !Objects.equals(note.getUserId(), userId)){
            return Responses.notFound("No existe esa nota");
        }
        if(service.delete(id) != null){
            return ResponseEntity.ok("Nota eliminada correctamente");
        }
        return Responses.badRequest("Error al eliminar la nota");
    }

    /**
     * Este metodo permite actualizar totalmente una nota
     * @param token es el token de autenticacion del usuario que quiere modificar la nota
     * @param note es la nota con los nuevos datos
     * @return un ResponseEntity indicando que se ha modificado correctamente la nota o que ha ocurrido algun error
     */
    @PutMapping("/")
    public ResponseEntity<String> update(@RequestHeader("token") String token, @RequestBody Note note){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Note dbNote = service.findById(note.getId());
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        if(!Objects.equals(dbNote.getUserId(), userId)){
            return Responses.badRequest("No existe una nota con ese id");
        }
        if(service.update(note) != null){
            return ResponseEntity.ok("Nota modificada correctamente");
        }
        return Responses.badRequest("Error al modificar la nota");
    }

    /**
     * Este metodo permite modificar el nombre de una nota
     * @param token es el token de autenticacion del usuario que quiere modificar la nota
     * @param title es el nuevo titulo que le vamos a asignar a la nota
     * @param id es el id de la nota a la que le queremos modificar el titulo
     * @return un ResponseEntity indicando que se ha modificado el titulo de la nota o de que ha ocurrido algun error
     */
    @PatchMapping("/update/title/{id}")
    public ResponseEntity<String> updateName(@RequestHeader("token") String token ,@RequestBody String title,  @PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Note dbNote = service.findById(id);
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        if(service.verify(title,userId)){
            return Responses.conflict("Ya existe una nota con ese titulo");
        }
        if(!Objects.equals(dbNote.getUserId(), userId)){
            return Responses.badRequest("No existe una nota con ese id");
        }
        dbNote.setTitle(title);
        if(service.save(dbNote) != null){
            return ResponseEntity.ok("Titulo modificado correctamente");
        }
        return Responses.badRequest("Error al modificar el titulo de la nota");
    }
    /**
     * Este metodo permite modificar el contenido de una nota
     * @param token es el token de autenticacion del usuario que quiere modificar la nota
     * @param content es el nuevo contenido que le vamos a asignar a la nota
     * @param id es el id de la nota a la que le queremos modificar el contenido
     * @return un ResponseEntity indicando que se ha modificado el contenido de la nota o de que ha ocurrido algun error
     */
    @PatchMapping("/update/content/{id}")
    public ResponseEntity<String> updateContent(@RequestHeader("token") String token ,@RequestBody String content,  @PathVariable Long id){
        if(!jwtUtil.verify(token)){
            return Responses.forbidden("Token de autenticacion invalido");
        }
        Note dbNote = service.findById(id);
        Long userId = Long.parseLong(jwtUtil.getKey(token));
        if(!Objects.equals(dbNote.getUserId(), userId)){
            return Responses.badRequest("No existe una nota con ese id");
        }
        dbNote.setContent(content);
        if(service.save(dbNote) != null){
            return ResponseEntity.ok("Contenido modificado correctamente");
        }
        return Responses.badRequest("Error al modificar el contenido de la nota");
    }
}
