package com.ams;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class logout extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("role").equals("faculty")) {
            session.removeAttribute("subject");
        }
        if (session.getAttribute("role").equals("student")) {
            session.removeAttribute("name");
        }
        session.removeAttribute("class");
        session.removeAttribute("role");
        session.removeAttribute("user");
        session.invalidate();
        resp.sendRedirect("./login.jsp");
    }
}
