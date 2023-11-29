package ru.clevertec.starter.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.lang.NonNull;
import ru.clevertec.starter.annotation.SessionAware;
import ru.clevertec.starter.property.SessionAwareProperties;
import ru.clevertec.starter.sevice.SessionAwareInterceptor;
import ru.clevertec.starter.sevice.SessionAwareService;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SessionAwareBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private final Map<String, Class<?>> beanNamesWithAnnotatedMethodsMap = new HashMap<>();
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(SessionAware.class))
                .forEach(method -> beanNamesWithAnnotatedMethodsMap.put(beanName, clazz));
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return Optional.ofNullable(beanNamesWithAnnotatedMethodsMap.get(beanName))
                .map(clazz -> getSessionAwareProxy(bean))
                .orElse(bean);
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private Object getSessionAwareProxy(Object bean) {
        SessionAwareService sessionAwareService = beanFactory.getBean(SessionAwareService.class);
        SessionAwareProperties sessionAwareProperties = beanFactory.getBean(SessionAwareProperties.class);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new SessionAwareInterceptor(bean, sessionAwareService, sessionAwareProperties, beanFactory));
        return isPresentDefaultConstructor(bean)
                ? enhancer.create()
                : enhancer.create(getNotDefaultConstructorArgTypes(bean), getNotDefaultConstructorArgs(bean));
    }

    private boolean isPresentDefaultConstructor(Object bean) {
        return Arrays.stream(bean.getClass().getConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

    private Class<?>[] getNotDefaultConstructorArgTypes(Object bean) {
        return Arrays.stream(bean.getClass().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .map(Constructor::getParameterTypes)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Object[] getNotDefaultConstructorArgs(Object bean) {
        Class<?>[] constructorArgTypes = getNotDefaultConstructorArgTypes(bean);
        return Arrays.stream(constructorArgTypes)
                .map(beanFactory::getBean)
                .toArray();
    }

}
