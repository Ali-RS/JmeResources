package org.jmonkey.server.servlets.html.controllers;

import org.jmonkey.server.UserNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;
import org.jmonkey.server.InsufficientPermissionException;
import org.jmonkey.server.servlets.html.WebPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author jayfella
 */

@ControllerAdvice
public class ExceptionController extends WebPage {


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFound(HttpServletRequest request) throws IOException {

        User user = UserManager.fromCookies(request.getCookies());
        
        ModelAndView model = new ModelAndView("/errors/not_found");
        this.addDefaultPageVariables(model, user, "Page Not Found");
        
        return model;
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public void handleUserNotFound(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {

        response.sendError(500, ex.getMessage());
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleInsufficientPermission(HttpServletRequest request, Exception ex) throws UnsupportedEncodingException {
        
        User user = UserManager.fromCookies(request.getCookies());
        
        ModelAndView model = new ModelAndView("/admin/insufficient_permission");
        
        model.setViewName("/errors/insufficient_permission");
        this.addDefaultPageVariables(model, user, "Insufficient Permission");
        model.addObject("errorString", ex.getMessage());
        
        return model;
    }
}
