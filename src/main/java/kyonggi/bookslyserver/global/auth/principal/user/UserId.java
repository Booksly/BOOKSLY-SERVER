package kyonggi.bookslyserver.global.auth.principal.user;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // --- 1
@Retention(RetentionPolicy.RUNTIME) // --- 2
@Parameter(hidden = true)
public @interface UserId {
}
