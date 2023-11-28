package ru.clevertec.starter.sevice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import ru.clevertec.starter.annotation.SessionAware;
import ru.clevertec.starter.exception.BlackListException;
import ru.clevertec.starter.exception.SessionAwareException;
import ru.clevertec.starter.model.Session;
import ru.clevertec.starter.property.SessionAwareProperties;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class SessionAwareInterceptor implements MethodInterceptor {

    private final Object originalBean;
    private final SessionService sessionService;
    private final SessionAwareProperties sessionAwareProperties;
    private final BeanFactory beanFactory;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (method.isAnnotationPresent(SessionAware.class)) {
            SessionAware annotation = method.getAnnotation(SessionAware.class);
            Set<String> mergedBlackSet = getMergedBlackList(annotation);
            Object[] newArgs = Optional.of(args)
                    .map(this::checkForSessionInParams)
                    .map(this::checkForMoreThanOneNonBlankFieldLogin)
                    .map(this::getLoginIfExists)
                    .filter(login -> !login.isEmpty())
                    .map(login -> checkLoginForBlackList(login, mergedBlackSet))
                    .map(login -> getNewArgs(args, login))
                    .orElseThrow(() ->
                            new SessionAwareException("Must be an object with a login non-blank field in the parameters"));
            return method.invoke(originalBean, newArgs);
        }
        return method.invoke(originalBean, args);
    }

    private Set<String> getMergedBlackList(SessionAware annotation) {
        String[] blackArray = annotation.blackList();
        Set<String> blackSet = sessionAwareProperties.getBlackList();
        Class<? extends BlackListHandler>[] blackHandlerArray = annotation.blackListHandlers();
        Set<Class<? extends BlackListHandler>> blackHandlerSet = sessionAwareProperties.getBlackListHandlers();
        Set<String> mergedBlackHandlers = Stream.concat(blackHandlerSet.stream(), Arrays.stream(blackHandlerArray))
                .map(beanFactory::getBean)
                .map(BlackListHandler::getBlackList)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        return Stream.concat(Stream.concat(Arrays.stream(blackArray), blackSet.stream()), mergedBlackHandlers.stream())
                .collect(Collectors.toSet());
    }

    private Object[] checkForSessionInParams(Object[] args) {
        if (Arrays.stream(args).noneMatch(Session.class::isInstance)) {
            throw new SessionAwareException("Must be a Session object in the parameters");
        }
        return args;
    }

    private Object[] checkForMoreThanOneNonBlankFieldLogin(Object[] args) {
        if (getFilteredStream(args).count() > 1) {
            throw new SessionAwareException("More than one objects with non-blank field login detected");
        }
        return args;
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
                        .filter(method -> arg.getClass().isRecord()
                                ? "login".equals(method.getName())
                                : "getLogin".equals(method.getName()))
                        .map(method -> {
                            try {
                                return method.invoke(arg);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new SessionAwareException("The object must be class or record and have getters");
                            }
                        })
                        .map(Object::toString)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining());
    }

    private String checkLoginForBlackList(String login, Set<String> mergedSet) {
        if (mergedSet.contains(login)) {
            throw new BlackListException("%s is in black list for Sessions".formatted(login));
        }
        return login;
    }

    private Object[] getNewArgs(Object[] args, String login) {
        return Arrays.stream(args)
                .map(arg -> arg instanceof Session
                        ? sessionService.findByLogin(login)
                        : arg)
                .toArray();
    }

}
