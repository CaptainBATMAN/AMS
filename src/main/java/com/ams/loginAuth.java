package com.ams;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginAuth extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usrname = request.getParameter("usrname");
        String pwd = request.getParameter("pwd");

        if(usrname.equals("mohan") && pwd.equals("batman")){

            HttpSession session = request.getSession();
            session.setAttribute("username", usrname);
            response.sendRedirect("./secure/studentHome.jsp");   
        }
        else{
            response.sendRedirect("login.jsp");
        }
    }
}
