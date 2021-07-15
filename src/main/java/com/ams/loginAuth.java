package com.ams;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginAuth extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usrname = req.getParameter("usrname");
        String pwd = req.getParameter("pwd");

        if(usrname.equals("mohan") && pwd.equals("batman")){

            HttpSession session = req.getSession();
            session.setAttribute("username", usrname);
            resp.sendRedirect("./secure/studentHome.jsp");   
        }
        else{
            resp.sendRedirect("login.jsp");
        }
    }
}
