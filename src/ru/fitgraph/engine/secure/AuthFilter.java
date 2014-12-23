package ru.fitgraph.engine.secure;

import ru.fitgraph.engine.secure.exceptions.ForbiddenException;
import ru.fitgraph.engine.secure.exceptions.NotAuthorizedException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by melges on 18.12.14.
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException, WebApplicationException {
        // Get method which will be invoked by jersey if we don't
        Method resourceMethod = resourceInfo.getResourceMethod();

        if(resourceMethod.isAnnotationPresent(PermitAll.class))
            return; // Simply return and continue work if resource available for all

        if(resourceMethod.isAnnotationPresent(DenyAll.class))
            throw new ForbiddenException("Forbidden. Denied for all.");

        Map<String, Cookie> cookieMap = requestContext.getCookies();
        Cookie vkIdCookie = cookieMap.get("vkId");
        if(vkIdCookie == null)
            throw new NotAuthorizedException("No vkId in cookie.");
        Long vkId =Long.getLong(vkIdCookie.getValue());
        if(vkId == null)
            throw new NotAuthorizedException("No vkId in cookie.");
        Cookie sessionCookie = cookieMap.get("JSESSIONID");
        if(sessionCookie == null)
            throw new NotAuthorizedException("No vkId in cookie.");
        String sessionSecret = sessionCookie.getValue();


        if(resourceMethod.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowed = resourceMethod.getAnnotation(RolesAllowed.class);
            String[] allowedRoles = rolesAllowed.value();

            // TODO: Add user group support

            return;
        }

        if(AuthController.isSessionCorrect(vkId, sessionSecret))
            throw new NotAuthorizedException("Session id incorrect");

    }
}
