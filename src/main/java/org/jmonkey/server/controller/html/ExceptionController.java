package org.jmonkey.server.controller.html;

import org.jmonkey.server.UserNotFoundException;
import org.jmonkey.server.website.template.TemplateFileNotFoundException;
import org.jmonkey.server.website.template.TemplatedWebPage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jayfella
 */

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public String handleNoHandlerFound(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateFileNotFoundException {

        return new TemplatedWebPage("The specified page does not exist.")
                .setPageTitle("Admin Control Panel")
                .useSessionData(request.getCookies())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public void handleUserNotFound(HttpServletRequest request, HttpServletResponse response, UserNotFoundException ex) throws IOException {

        response.sendError(500, ex.getMessage());
    }

}
