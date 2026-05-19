package com.lavanderiaonline.infrastructure.config;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional(readOnly = true, timeout = 30)
public @interface ReadTx {
}