package com.mangione.continuous.demos.ranklib;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HyperParameters {
    final static Predicate<Field> publicStaticPredicate = (f)-> ((Predicate<Integer>) Modifier::isPublic)
            .and(Modifier::isStatic)
            .test(f.getModifiers());

    final static Predicate<Field> notFinalPredicate = (f)->!Modifier
            .isFinal(f.getModifiers());

    final static Predicate<Field> floatIntegerStringPredicate = f-> f.getType().isAssignableFrom(Integer.TYPE)||
                    f.getType().isAssignableFrom(Float.TYPE)||
                    f.getType().isAssignableFrom(String.class);

    final Class<?> theClass;

    final Map<String,Field> fields;

    public HyperParameters(Class<?> theClass){
        this.theClass = theClass;
        fields = Arrays.stream(theClass.getFields())
                .filter(publicStaticPredicate.and(notFinalPredicate).and(floatIntegerStringPredicate).and(Field::isAccessible)).collect(Collectors.toMap(
                Field::getName,
                Function.identity()
        ));
    }

    public Map<String, Optional<Object>> getValues(){
        Map<String, Optional<Object>> map = fields.keySet().stream().collect(Collectors.toMap(
                Function.identity(),
                (name) -> {
                    try {
                        Object o = fields.get(name).get(null);
                        return Optional.of(o);
                    } catch (IllegalAccessException e) {
                        return Optional.empty();
                    }
                }
        ));
        return Collections.unmodifiableMap(map);
    }

    public Map<String, Class<?>> getTypes(){
        return fields.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), (f)->fields.get(f).getType()));
    }

    private Field getField(String fieldName) throws IllegalArgumentException{
        if(!fields.containsKey(fieldName))
            throw new IllegalArgumentException(String.format("Field name %s not found.",fieldName));
        return fields.get(fieldName);
    }

    public Object get(String fieldName) throws IllegalAccessException {
        Field field = getField(fieldName);
        return field.get(null);
     }

    public void set(String fieldName, Object value) throws IllegalAccessException{
        Field field = getField(fieldName);
        if(field.getType().isAssignableFrom(value.getClass())) field.set(null,value);
        else throw new IllegalAccessException();
    }

    public void update(Map<String,Object> updates){
        try {
            for (String key : updates.keySet()) {
                if(!fields.containsKey(key)) throw new IllegalStateException();
                Object value = updates.get(key);
                set(key, value);
            }
        }catch (IllegalAccessException e) {
            throw new IllegalStateException();
        }
    }
}
