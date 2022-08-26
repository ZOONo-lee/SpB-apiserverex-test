package org.zerock.apiserverex.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class CustomErrorController implements ErrorController {
  @GetMapping("/error")
  public String handleError(HttpServletRequest request, Model model){
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if(status != null){
      int statusCode = Integer.valueOf(status.toString());
      log.info("statuCode: " + statusCode);
      if(statusCode == HttpStatus.NOT_FOUND.value()){
        model.addAttribute("msg", status.toString());
        return "exceptions/notFound";
      } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
        model.addAttribute("msg", status.toString());
        return "exceptions/serverError";
      }
    }
    model.addAttribute("msg", status.toString());
    return "exceptions/serverError";
  }
}
