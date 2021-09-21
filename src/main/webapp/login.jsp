<%@ page language="java" contentType="text/html; charset = UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.ams.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>



<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AMS LogIn</title>
    <link rel="stylesheet" href="./custom_styles/login.css">
    <link rel="stylesheet" href="./imported/bootstrap.min.css">
</head>

<body>

    <% 
    String status = (String) request.getAttribute("status");
    if(status == null){
        status = "";
    }
    %>

   


    <!-- <nav class="
            navbar
            sticky-top
            navbar-expand-lg 
            navbar-expand-sm
            navbar-dark">
        <a class="navbar-brand p-0" href="#">Gmeet AMS</a>
    </nav> -->

    <div class="mainCard">
        <div class="box ">
            <h2>Gmeet AMS Login</h2>
            <form action="./loginAuth" method="POST">
                <div class="inputBox">
                    <input id="emailId" name="emailId" type="email" required="">
                    <label for="">Email</label>
                </div>
                <div class="inputBox">
                    <input  name="pwd" type="password" type="password" required="">
                    <label  id="pwd" for="">Password</label>
                </div>
                <div>
                    <h6 class="text-danger" style="text-align: center;"> <%= status%>  </h6>
                </div>
                <input type="submit" name="" id="" value="Login" class="btn btn-sm btn-block btn-outline-dark">
            </form>
        </div>
    </div>
       


   
</body>
