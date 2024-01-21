package viserrys;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        // Handle the exception, log it, or perform any necessary actions
        // For example, you can add an error message to the model
        // Redirect to an error page or another appropriate page
        return "redirect:/error";
    }
}
