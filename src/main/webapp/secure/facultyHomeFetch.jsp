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

            <!-- ! custom scripts and stylesheets -->
            <link rel="stylesheet" href="../custom_styles/facultyHome.css" />
            <script src="../custom_scripts/faculty_home_fetch.js"></script>

            <!-- ! bootstrap css and js -->
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
                                            <button class="p-2 navbar-toggler" type="button" data-toggle="collapse"
                                                data-target="#navbarNav" aria-controls="navbarTogglerDemo01"
                                                aria-expanded="false" aria-label="Toggle navigation">
                                                <i class="fas fa-caret-down"></i>
                                            </button>
                                            <div class="collapse navbar-collapse" id="navbarNav">
                                                <ul class="navbar-nav ml-auto mt-2 mt-lg-0 mt-sm-0 my-10">
                                                    <li class="nav-item text-white mr-4" style="text-decoration: none;">
                                                        <a class="nav-link active"
                                                            href="./facultyHomeUpdate.jsp">Update</a>
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
                                                <h3>View Attendence Reports</h3>
                                                <div class="form-group row">
                                                    <div class="col-lg-6 col-md-6">
                                                        <label for="fromDate" class="p-0 col-form-label"> <span
                                                                class="font-weight-bolder">From
                                                                Date:</span>
                                                        </label>
                                                        <input type=" text "
                                                            class="input-sm form-control form-control-sm"
                                                            id="fromDate" />
                                                    </div>
                                                    <div class="col-lg-6 col-md-6">
                                                        <label for="toDate" class="p-0 col-form-label"> <span
                                                                class="font-weight-bolder">To
                                                                Date:</span>
                                                        </label>
                                                        <input type="text "
                                                            class="input-sm form-control form-control-sm" id="toDate" />

                                                    </div>
                                                </div>

                                                <div class="form-group row">
                                                    <div class="col-lg-6 col-md-6" style="margin-top: 10px;">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <label class="input-group-text text-dark"
                                                                    for="class">Class:</label>
                                                            </div>
                                                            <select name="class" class="custom-select" id="class">

                                                                <% for(int i=0; i < classes.size();i++) {%>
                                                                    <option class="custom-options"
                                                                        value="<%= classes.get(i)%>">
                                                                        <%= classes.get(i)%>
                                                                    </option>
                                                                    <% } %>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div id="subjectCard" class="col-lg-6 col-md-6"
                                                        style="margin-top: 10px;">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <label class="input-group-text text-dark"
                                                                    for="subject">Subject :</label>
                                                            </div>
                                                            <select name="subject" class="custom-select" id="subject">
                                                                <% for(int i=0; i < subjects.size();i++) {%>
                                                                    <option class="custom-options"
                                                                        value="<%= subjects.get(i)%>">
                                                                        <%= subjects.get(i)%>
                                                                    </option>
                                                                    <% } %>
                                                                        <% } %>
                                                                            </option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div id="customTopicCard" class="col-lg-6  col-md-6 d-none"
                                                        style="margin-top: 10px;">
                                                        <input type="text" class="input-sm form-control"
                                                            id="customTopic" placeholder="Custom Topic"
                                                            style="text-transform:uppercase" />
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-lg-6 col-md-6">
                                                        <div class="form-check">
                                                            <input name="customTopicCheck" class="form-check-input"
                                                                type="checkbox" value="" id="customTopicCheck">
                                                            <label class="form-check-label" for="flexCheckDefault">
                                                                Custom Topic
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <button id="fetchButton" type="submit" style="cursor: pointer"
                                                    class="btn btn-sm btn-outline-success">
                                                    View
                                                </button>
                                            </div>

                                            <div id="progressBarCard" class="d-none"
                                                style="width: 100%; margin: 0 auto;">
                                                <div id="progress" class="card-body">
                                                    <div class="progress">
                                                        <div id="progressBar"
                                                            class="progress-bar progress-bar-striped progress-bar-animated bg-dark"
                                                            role="progressbar" aria-valuenow="75" aria-valuemin="0"
                                                            aria-valuemax="100" style="width: 0%"></div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div id="attendanceReports" style="margin-top:3em;">
                                                <div class="d-none" id="statsCard" style="width: 60%; margin: 0 auto;">
                                                    <div id="stats" class="card-body">
                                                        <table style="width: 100%; text-align: center;">
                                                            <tr style="font: 800 40px system-ui;">
                                                                <td id="totalStudents" class="text-primary"></td>
                                                                <td id="presentStudents" class="text-success"></td>
                                                                <td id="absentStudents" class="text-danger"></td>
                                                            </tr>
                                                            <tr style="font-size: 1em;">
                                                                <td class="text-primary">Total</td>
                                                                <td class="text-success">Present</td>
                                                                <td class="text-danger">Absent</td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                </div>

                                                <div id="attendanceReportsCard" class="d-none">
                                                    <div id="noAttendanceRecords" class="d-none">
                                                        <h6 class="text-danger text-center font-weight-bolder">
                                                            No Attendance Records found.
                                                        </h6>
                                                    </div>
                                                    <div id="renderAttendanceReports" class="d-none">
                                                        <table id="data-table"
                                                            class="ui celled table table-bordered text-center table-condensed table-hover no-footer"
                                                            style="width: 100%; margin: 0 auto">
                                                            <thead>
                                                                <tr>
                                                                    <th>#</th>
                                                                    <th>Student Email</th>
                                                                    <th>Meeting ID</th>
                                                                    <th>Class</th>
                                                                    <th>Subject (Topic)</th>
                                                                    <th>Duration</th>
                                                                    <th>Attendance</th>
                                                                </tr>
                                                            </thead>
                                                        </table>
                                                    </div>

                                                    <div id="renderAttendanceReportsRange" class="d-none"
                                                        style="margin-top: 1em;">

                                                    </div>
                                                </div>
                                            </div>



                                        </div>

                                    </div>
        </body>

        </html>