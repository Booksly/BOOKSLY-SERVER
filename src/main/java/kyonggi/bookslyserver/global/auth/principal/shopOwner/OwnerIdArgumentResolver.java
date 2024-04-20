package kyonggi.bookslyserver.global.auth.principal.shopOwner;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.global.auth.principal.user.OAuthPrincipalDetails;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class OwnerIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasUserIdAnnotation = parameter.hasParameterAnnotation(OwnerId.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasUserIdAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        OwnerPrincipalDetails principalDetails = (OwnerPrincipalDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        ShopOwner shopOwner = principalDetails.getShopOwner();
        return shopOwner.getId();
    }
}
