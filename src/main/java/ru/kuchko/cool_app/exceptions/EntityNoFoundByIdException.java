package ru.kuchko.cool_app.exceptions;

public class EntityNoFoundByIdException extends EntityNotFoundException{
    public EntityNoFoundByIdException(String entity, Integer id) {
        super(entity + " with id " + id + " not found");
    }
}
