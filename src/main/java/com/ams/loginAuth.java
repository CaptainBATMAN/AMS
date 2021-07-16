package com.ams;

import java.io.Console;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginAuth extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginId = request.getParameter("emailId");
        String pwd = request.getParameter("pwd");

        String basicAdminLoginId = "s160132@rguktsklm.ac.in";
        
        if(loginId.equals(basicAdminLoginId) && pwd.equals("batman")){

            HttpSession session = request.getSession();
            session.setAttribute("user", loginId);
            response.sendRedirect("./secure/studentHome.jsp");   
        }
        else{
            response.sendRedirect("login.jsp");
        }
    }
}
