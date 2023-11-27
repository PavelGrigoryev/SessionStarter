package ru.clevertec.starter.sevice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import ru.clevertec.starter.annotation.SessionAware;
import ru.clevertec.starter.exception.SessionAwareException;
import ru.clevertec.starter.model.Session;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
public class SessionAwareInterceptor implements MethodInterceptor {

    private final Object originalBean;
    private final SessionService sessionService;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.isAnnotationPresent(SessionAware.class)) {
            if (getFilteredStream(args).count() > 1) {
                throw new SessionAwareException("More than one objects with non-blank field login detected");
            }
            String login = getLoginIfExists(args);
            if (!login.isEmpty()) {
                Object[] newArgs = Arrays.stream(args)
                        .map(arg -> arg instanceof Session
                                ? sessionService.findByLogin(login)
                                : arg)
                        .toArray();
                return method.invoke(originalBean, newArgs);
            } else {
                throw new SessionAwareException("Must be an object with a login non-blank field in the parameters");
            }
        }
        return method.invoke(originalBean, args);
    }

    private Stream<Object> getFilteredStream(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> !(arg instanceof Session))
                .filter(Objects::nonNull)
                .filter(arg -> Arrays.stream(arg.getClass().getDeclaredFields())
                        .map(Field::getName)
                        .anyMatch("login"::equals));
    }

    private String getLoginIfExists(Object[] args) {
        return getFilteredStream(args)
                .map(arg -> Arrays.stream(arg.getClass().getMethods())
                        .filter(m -> arg.getClass().isRecord()
                                ? "login".equals(m.getName())
                                : "getLogin".equals(m.getName()))
                        .map(m -> {
                            try {
                                return m.invoke(arg);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new SessionAwareException("The object must have getters");
                            }
                        })
                        .map(Object::toString)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining());
    }

}
