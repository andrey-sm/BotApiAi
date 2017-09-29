package pro.smartum.botapiai.annotations.swagger;


import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pro.smartum.botapiai.dto.rs.ErrorRs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
@Target(ElementType.METHOD)
@ApiResponses(value = {@ApiResponse(code = 500, message = "Internal error. Check json error code", response = ErrorRs.class)})
public @interface SwaggerResponseStatus {}
