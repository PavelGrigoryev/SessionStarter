package ru.clevertec.starter.annotation;

import ru.clevertec.starter.sevice.handler.BlackListHandler;
import ru.clevertec.starter.sevice.handler.impl.PropertyBlackListHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionAware {

    String[] blackList() default {};

    Class<? extends BlackListHandler>[] blackListHandlers() default {PropertyBlackListHandler.class};

}
