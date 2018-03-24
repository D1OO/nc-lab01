package main;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;

import static org.apache.commons.lang3.reflect.MethodUtils.getMethodsListWithAnnotation;

/**
 * Class that provides functionality based on reflection.
 * <p>
 * Based on methods from commons-lang3-reflect and code-reflections libraries.
 *
 * @author Dmytro Storozhenko
 * @version 1.0
 */
public final class ReflectionUtils {

    /**
     * Gets all subclasses of the passed class with {@link Reflections#getSubTypesOf(Class)} and returns them without abstract classes.
     *
     * @param packagePrefix the package prefix to be passed in {@link Reflections} object
     * @param parent the class, non-abstract subclasses of which are to be returned
     * @param <T> the type of the parent class
     * @return The {@link LinkedHashSet} of {@code <Class<? extends T>>} type, where T - type of the parent class
     * @see Reflections#getSubTypesOf(Class)
     */
    public static <T> LinkedHashSet<Class<? extends T>> getNonAbstrSubTypesOfClass(String packagePrefix, Class<T> parent) {
        LinkedHashSet<Class<? extends T>> allSubTypes = new LinkedHashSet<>(new Reflections(packagePrefix).getSubTypesOf(parent));
        allSubTypes.removeIf((Class c) -> Modifier.isAbstract(c.getModifiers()));
        return allSubTypes;
    }

    /**
     * Returns a list of methods from the passed class, that are annotated with the passed annotation.
     *
     * @param classWithAnnotation the class that contains annotated methods to be found
     * @param annotation the {@link Annotation} that must be present on a method
     * @return a {@link LinkedHashSet}<{@link Method}>
     * @see org.apache.commons.lang3.reflect.MethodUtils#getMethodsListWithAnnotation(Class, Class)
     */
    public static LinkedHashSet<Method> getAnnotatedMethods(Class classWithAnnotation, Class<? extends Annotation> annotation) {
        return new LinkedHashSet<>(getMethodsListWithAnnotation(classWithAnnotation, annotation));
    }
}

