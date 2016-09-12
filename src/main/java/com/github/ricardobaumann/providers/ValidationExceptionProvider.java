/**
 * 
 */
package com.github.ricardobaumann.providers;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author ricardobaumann
 *
 */
@Provider
public class ValidationExceptionProvider implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(422).build();
    }

}
