<%@ page language="java" contentType="text/html; charset = UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.ams.*" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Faculty</title>
            <script src="../imported/jquery-3.6.0.js"></script>
            <script src="../imported/moment.js"></script>

            <!-- ! Custom styles and scripts -->
            <link rel="stylesheet" href="../custom_styles/customStyles.css" />
            <script src="../custom_scripts/faculty_home_update.js"></script>
          
            <!-- ! BootStrap css and js -->
            <link rel="stylesheet" href="../imported/bootstrap.min.css" />
            <script src="../imported/bootstrap.4.4.1.min.js"></script>

            <!-- ! for Datepicker -->
            <link rel="stylesheet" href="../imported/datepicker.css" />
            <script src="../imported/bootstrap-datepicker.js"></script>

            <!-- ! for DataTables -->
            <link rel="stylesheet" type="text/css" href="./../DataTables/datatables.min.css" />
            <script type="text/javascript" src="./../DataTables/datatables.min.js"></script>
        </head>

        <body>
            <% response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); String URL="" ;
                if(session.getAttribute("user")==null){ URL="/ams/login.jsp" ; response.sendRedirect(URL); } else
                if(!session.getAttribute("role").equals("faculty")){ URL="/ams/login.jsp" ; response.sendRedirect(URL);
                } %>

                <div class="div-center">
                    <div id="nav-div" style="margin-bottom: 0em">
                        <nav class="
            navbar
            sticky-top
            navbar-expand-lg navbar-dark
            bg-dark
            font-poppins
          " style="position: relative">
                            <a class="navbar-brand p-0" href="./facultyHomeUpdate.jsp">AMS</a>
                            <a class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                                <span id="toggle-button" class="navbar-toggler-icon"></span>
                            </a>
                            <div class="navbar-collapse collapse" id="navbarNav">
                                <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
                                    <li class="nav-item">
                                        <a class="nav-link" href="./facultyHomeFetch.jsp">Fetch</a>
                                    </li>
                                    <li class="nav-item active" >
                                        <a class="nav-link" style="color: #28a745 !important;" href="./facultyHomeUpdate.jsp">Update</a>
                                    </li>
                                    <li class="nav-item">
                                        <form action="../logout" method="POST">
                                            <button type="submit" id="logOutBtn" class="btn btn-danger btn-sm" href="#"
                                                style="
                      position: absolute;
                      right: 10px;
                      margin: auto 0;
                      bottom: 15px;
                    ">
                                                LogOut
                                            </button>
                                        </form>
                                    </li>
                                </ul>
                            </div>
                        </nav>
                    </div>

                    <div id="infoCard" style="width: 40%; margin: 2em auto" class="d-none">
                        <div class="card border-dark">
                            <div class="card-header bg-dark p-1">
                                <h6 class="modal-title text-white font-weight-bold m-0">Info</h6>
                            </div>
                            <div class="card-body">
                                <h6 style="padding: 0" class="text-danger">
                                    Please follow the below instructions:
                                </h6>
                                <p class="text-dark">
                                    1. All fields are required to update the Student Data in the
                                    DataBase. <br />
                                    2.Please check the details once again before submitting the data.
                                </p>
                            </div>
                        </div>
                    </div>


                    <div class="container">
                        <div class="d-flex row justify-content-center">
                            <div class="col-12 col-lg-10 mt-2">
                                <div class="card border-dark">
                                    <div class="card-header bg-dark p-1">
                                        <h6 class="modal-title text-white font-weight-bold m-0">
                                            Update Attendance Reports
                                        </h6>
                                    </div>

                                    <div class="card-body" style="width: 100%; margin: 0 auto">
                                        <div class="form-group">
                                            <div class="form-row">
                                                <div class="form-group col-md-6">
                                                    <p class="font-weight-bolder">
                                                        Date:
                                                        <input type="text" class="input-sm form-control" id="date" />
                                                    </p>
                                                </div>
                                                <!-- ! Google meet code -->
                                                <div class="form-group col-md-6">
                                                    <p class="font-weight-bolder">
                                                        Google Meet Code :
                                                        <input id="gmeetcode" class="input-sm form-control" type="text"
                                                            maxlength="10" name="gmeetcode" />
                                                    </p>
                                                </div>
                                                <!-- ! from time -->
                                                <div class="form-group col-md-6">
                                                    <p class="font-weight-bolder">
                                                        From Time:
                                                        <input type="time" class="input-sm form-control"
                                                            id="fromTime" />
                                                    </p>
                                                </div>
                                                <!-- ! to time -->
                                                <div class="form-group col-md-6">
                                                    <p class="font-weight-bolder">
                                                        To Time:
                                                        <input type="time" class="input-sm form-control" id="toTime" />
                                                    </p>
                                                </div>
                                                <!-- !subject -->
                                                <div class="form-group col-md-6">
                                                    <p class="font-weight-bolder">
                                                        Subject:
                                                        <input type="text" class="input-sm form-control" id="subject" />
                                                    </p>
                                                </div>
                                                <!-- ! class -->
                                                <div class="form-group col-md-6">
                                                    <p class="font-weight-bolder">
                                                        Class:
                                                        <input type="text" class="input-sm form-control" id="class" />
                                                    </p>
                                                </div>
                                                <!-- ! Period number Radio buttons -->
                                                <div class="form-group" style="margin-left: 0">
                                                    <div class="form-check">
                                                        <label for="period"
                                                            style="margin-right: 30px; margin-left: 15px">Period :
                                                        </label>
                                                        <input class="form-check-input" type="radio" name="period"
                                                            id="exampleRadios1" value="P1" />
                                                        <label class="form-check-label" for="exampleRadios1">
                                                            Period 1
                                                        </label>
                                                    </div>
                                                    <div class="form-check" style="margin-left: 15px">
                                                        <input class="form-check-input" type="radio" name="period"
                                                            id="exampleRadios2" value="P2" />
                                                        <label class="form-check-label" for="exampleRadios2">
                                                            Period 2
                                                        </label>
                                                    </div>
                                                    <div class="form-check" style="margin-left: 15px">
                                                        <input class="form-check-input" type="radio" name="period"
                                                            id="exampleRadios3" value="P3" />
                                                        <label class="form-check-label" for="exampleRadios3">
                                                            Period 3
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <button id="fetchButton" type="submit" style="
                    cursor: pointer;
                    margin-left: 1.2em;
                    margin-top: -1.5em;
                  " class="btn btn-success btn-sm">
                                            Update
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div style="margin-top: 4em">
                       <div class="d-none" id="updateReportCard">
                           <h3 id="updateReport"></h3>
                       </div>
                    </div>
                </div>
        </body>

        </html>