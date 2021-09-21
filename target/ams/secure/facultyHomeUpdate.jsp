<%@ page language="java" contentType="text/html; charset = UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.ams.*,java.util.*" %>

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
            <link rel="stylesheet" href="../custom_styles/facultyHome.css" />
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

            <!-- ! for alertify -->
            <link rel="stylesheet" href="../alertify/alertify.core.css" />
            <link rel="stylesheet" href="../alertify/alertify.default.css" id="linkID" />
            <script src="../alertify/alertify.min.js"></script>

        </head>

        <body>
            <% response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); String URL="" ;
                if(session.getAttribute("user")==null){ URL="/ams/login.jsp" ; response.sendRedirect(URL); } else
                if(!session.getAttribute("role").equals("faculty")){ URL="/ams/login.jsp" ; response.sendRedirect(URL);
                } else{ ArrayList<String> subjects = new ArrayList<String>();
                    ArrayList<String> classes = new ArrayList<String>();
                            subjects = (ArrayList<String>) session.getAttribute("subject");
                                classes = (ArrayList<String>) session.getAttribute("class");
                                    %>

    <div id="nav-div">
        <nav class="p-1 navbar sticky-top navbar-expand-lg navbar-expand-md navbar-expand-sm navbar-dark"
            style="position: relative">
                <a class="navbar-brand p-0" href="#">Gmeet AMS</a>
                <button class="p-2 navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarTogglerDemo01" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fas fa-caret-down"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto mt-2 mt-lg-0 mt-sm-0 my-10">
                    <li class="nav-item text-white mr-4" style="text-decoration: none;">
                        <a class="nav-link active" href="./facultyHomeFetch.jsp">View</a>
                    </li>
                    <li class="nav-item my-auto">
                        <form action="../logout" method="POST">
                            <button type="submit" id="logOutBtn"
                                class="btn btn-danger btn-sm m-0 m-auto"
                                style="border-radius: 4px" href="#">
                                <i class="fas fa-sign-out-alt"></i>&nbsp;&nbsp;Log Out
                            </button>
                        </form>
                    </li>
                </ul>
            </div>
        </nav>
    </div>

     <!-- Page Content -->
     <div id="page-content-wrapper">
    
    <div class="container-fluid">
        <div id="view-attendance" class="">
            <h3>Update Attendance Data</h3>
            <div class="form-group row">
                <!-- ! Date -->
                <div class="col-lg-6 col-md-6">
                    <label for="date" class="p-0 col-form-label"> <span class="font-weight-bolder">Date:</span>
                    </label>
                    <input type="text" class="input-sm form-control form-control-sm" id="date" />
                </div>
                <!-- ! gmeetcode -->
                <div class="col-lg-6 col-md-6">
                    <label for="gmeetcode" class="p-0 col-form-label"> <span class="font-weight-bolder">Google
                            MEET Code:</span>
                    </label>
                    <input id="gmeetcode" class="input-sm form-control form-control-sm" type="text" maxlength="10"
                        name="gmeetcode" style="text-transform:uppercase" />
                </div>
            </div>
            <div class="form-group row">
                <!-- ! from time -->
                <div class="col-lg-6 col-md-6">
                    <label for="fromTime" class="p-0 col-form-label"> <span class="font-weight-bolder">From
                            Time:</span>
                    </label>
                    <input type="time" class="input-sm form-control form-control-sm" id="fromTime" />

                </div>
                <!-- ! to time -->
                <div class="col-lg-6 col-md-6">
                    <label for="toTime" class="p-0 col-form-label"> <span class="font-weight-bolder">To
                            Time:</span>
                    </label>
                    <input type="time" class="input-sm form-control form-control-sm" id="toTime" />

                </div>
            </div>
            <div class="form-group row">
                <!-- ! class -->
                <div class="col-lg-6 col-md-6" style="margin-top: 10px;">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <label class="input-group-text text-dark" for="class">Class:</label>
                        </div>
                        <select name="class" class="custom-select" id="class">
                            <% for(int i=0; i < classes.size();i++) {%>
                                <option class="custom-options" value="<%= classes.get(i)%>"><%= classes.get(i)%></option>
                            <% } %>
                        </select>
                    </div>
                </div>
                <!-- ! subject -->
                <div id="subjectCard" class="col-lg-6 col-md-6" style="margin-top: 10px;">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <label class="input-group-text text-dark" for="subject">Subject :</label>
                        </div>
                        <select name="subject" class="custom-select" id="subject">
                            <% for(int i=0; i < subjects.size();i++) {%><option class="custom-options" value="<%= subjects.get(i)%>">
                                <%= subjects.get(i)%></option>
                            <% } %>
                            <% } %>
                        </select>
                    </div>
                </div>
                <!-- ! custom topic  -->
                <div id="customTopicCard" class="col-lg-6 col-md-64 d-none" style="margin-top: 10px;">
                    <input type="text" class="input-sm form-control" id="customTopic" placeholder="Custom Topic"
                        style="text-transform:uppercase" />
                </div>
            </div>
            <div class="form-group row">
                <!-- ! custom topic checkbox -->
                <div class="col-lg-6 col-md-6">
                    <div class="form-check">
                        <input name="customTopicCheck" class="form-check-input" type="checkbox" value=""
                            id="customTopicCheck">
                        <label class="form-check-label" for="flexCheckDefault">
                            Custom Topic
                        </label>
                    </div>
                </div>
            </div>
        <!-- ! periods -->
        <div class="form-group row">
            <div class="form-check">
                <label for="period" style="margin-right: 30px; margin-left: 15px">Period
                    :
                </label>
                <input class="form-check-input" type="radio" name="period" id="exampleRadios1" value="P1" />
                <label class="form-check-label" for="exampleRadios1">
                    Period 1
                </label>
            </div>
            <div class="form-check" style="margin-left: 15px">
                <input class="form-check-input" type="radio" name="period" id="exampleRadios2" value="P2" />
                <label class="form-check-label" for="exampleRadios2">
                    Period 2
                </label>
            </div>
            <div class="form-check" style="margin-left: 15px">
                <input class="form-check-input" type="radio" name="period" id="exampleRadios3" value="P3" />
                <label class="form-check-label" for="exampleRadios3">
                    Period 3
                </label>
            </div>
        </div>
        <!-- ! info  -->
        <div class="form-group row">
            <div class="col-lg-12">
                <p class="text-muted">
                    <span class="fa fa-info"></span>&nbsp;&nbsp;&nbsp;Please check Class and Subject before
                    updating..!
                </p>
            </div>
        </div>
        <button id="updateBtn" type="submit" style="cursor: pointer" class="btn btn-sm btn-outline-success">
            Update
        </button>
    </div>
    <div id="attendanceReports" style="margin-top: 3.5em;">
        <div style="margin-top: 4em">
            <div class="" id="updateReportCard">
                <h4 id="updateReport" class="font-weight-bold"></h4>
            </div>
        </div>
    </div>
    </div>
    </div>

    </body>
</html>