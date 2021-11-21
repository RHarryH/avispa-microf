package com.avispa.microf.model.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public ModelAndView handleError(HttpServletRequest request) {
        ModelAndView errorPage = new ModelAndView("error/error");
        String errorMessage = "";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 400: {
                    errorMessage = "Http Error Code: 400. Bad Request";
                    break;
                }
                case 401: {
                    errorMessage = "Http Error Code: 401. Unauthorized";
                    break;
                }
                case 404: {
                    errorMessage = "Http Error Code: 404. Resource not found";
                    break;
                }
                case 500: {
                    errorMessage = "Http Error Code: 500. Internal Server Error";
                    break;
                }
                default:
                    errorMessage = "Http Error Code: " + statusCode;
            }
        }

        errorPage.addObject("errorMessage", errorMessage);

        return errorPage;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}