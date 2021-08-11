<%@ page language="java" contentType="text/html; charset = UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.ams.*,java.util.*" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <meta http-equiv="content-type" content="text/plain; charset=UTF-8"/>
            <title>Admin</title>
            <script src="../imported/jquery-3.6.0.js"></script>
            <script src="../imported/moment.js"></script>

            <!-- ! custom scripts and stylesheets -->
            <link rel="stylesheet" href="../custom_styles/customStyles.css" />
            <script src="../custom_scripts/admin_home.js"></script>

            <!-- ! bootstrap css and js -->
            <link rel="stylesheet" href="../imported/bootstrap.min.css" />
            <script src="../imported/bootstrap.4.4.1.min.js"></script>

            <!-- ! for Datepicker -->
            <link rel="stylesheet" href="../imported/datepicker.css" />
            <script src="../imported/bootstrap-datepicker.js"></script>

            <!-- ! for DataTables -->
             <!-- <link rel="stylesheet" href="./../imported/buttons.dataTables.min.css"> -->
            <link rel="stylesheet" type="text/css" href="./../DataTables/datatables.min.css" />
            <script type="text/javascript" src="./../DataTables/datatables.min.js"></script>
            <script src="./../imported/dataTables.buttons.min.js"></script> 

            <!-- ! for alertify -->
            <link rel="stylesheet" href="../alertify/alertify.core.css" />
            <link rel="stylesheet" href="../alertify/alertify.default.css" id="linkID" />
            <script src="../alertify/alertify.min.js"></script>


            <!-- ! for excel download -->
            <script src="../imported/jszip.min.js"></script>
            <script src="./../imported/buttons.html5.min.js"></script>
        </head>

        <body>
            <% response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
                String URL="" ;
                if(session.getAttribute("user")==null){ URL="/ams/login.jsp" ; response.sendRedirect(URL); } 
                else if(!session.getAttribute("role").equals("admin")){ 
                    URL="/ams/login.jsp" ; 
                    response.sendRedirect(URL);
                }
                else{ 
                    ArrayList<String> classes = new ArrayList<String>();
                    classes = (ArrayList<String>) session.getAttribute("class");
                    %>

                                    <div class="div-center">
                                        <div id="nav-div" style="margin-bottom: 0em">
                                            <nav class="
            navbar
            sticky-top
            navbar-expand-lg navbar-dark
            bg-dark
            font-poppins
          " style="position: relative">
                                                <a class="navbar-brand p-0" href="./adminHome.jsp">AMS</a>
                                                <a class="navbar-toggler" type="button" data-toggle="collapse"
                                                    data-target="#navbarNav" aria-controls="navbarNav"
                                                    aria-expanded="false" aria-label="Toggle navigation">
                                                    <span id="toggle-button" class="navbar-toggler-icon"></span>
                                                </a>
                                                <div class="navbar-collapse collapse" id="navbarNav">
                                                    <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
                                                        <li class="nav-item active">
                                                            <a class="nav-link" style="color: #28a745 !important;"
                                                                href="./adminHome.jsp">Admin</a>
                                                        </li>
                                                        <li class="nav-item">
                                                            <form action="../logout" method="POST">
                                                                <button type="submit" id="logOutBtn"
                                                                    class="btn btn-danger btn-sm" href="#" style="
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



                                        <div class="container">
                                            <div class="d-flex row justify-content-center">
                                                <div class="col-12 col-lg-10 mt-2">
                                                    <div class="card border-dark">
                                                        <div class="card-header bg-dark p-1">
                                                            <h6 class="modal-title text-white font-weight-bold m-0">
                                                                Fetch Attendance Reports
                                                            </h6>
                                                        </div>

                                                        <div class="card-body" style="width: 100%; margin: 0 auto">
                                                            <div class="form-group">
                                                                <div class="form-row">

                                                                    <!-- ! FromDate -->
                                                                    <div class="form-group col-md-6">
                                                                        <p class="font-weight-bolder">
                                                                            From Date:
                                                                            <input type="text"
                                                                                class="input-sm form-control"
                                                                                id="fromDate" />
                                                                        </p>
                                                                    </div>
                                                                    <!-- ! toDate -->
                                                                    <div class="form-group col-md-6">
                                                                        <p class="font-weight-bolder">
                                                                            To Date:
                                                                            <input type="text"
                                                                                class="input-sm form-control"
                                                                                id="toDate" />
                                                                        </p>
                                                                    </div>
                                                                   
                                                                    <!-- ! class -->
                                                                    <div class="form-group col-md-6"
                                                                        style="margin-top: 10px;">


                                                                        <div class="input-group">
                                                                            <div class="input-group-prepend">
                                                                                <label
                                                                                    class="input-group-text text-dark"
                                                                                    for="class">Class:</label>
                                                                            </div>
                                                                            <select name="class" class="custom-select"
                                                                                id="class">

                                                                                <% for(int i=0; i < classes.size();i++)
                                                                                    {%>
                                                                                    <option class="custom-options"
                                                                                        value="<%= classes.get(i)%>">
                                                                                        <%= classes.get(i)%>
                                                                                    </option>
                                                                                    <% } %>
                                                                                    <% } %>
                                                                            </select>
                                                                        </div>
                                                                    </div>

                                                                    

                                                                </div>
                                                            </div>
                                                            <button id="fetchButton" type="submit" style="
                    cursor: pointer;
                    margin-left: 1.2em;
                    margin-top: -1.5em;
                  " class="btn btn-success btn-sm">
                                                                View
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        
                                        <div id="progressBarCard" class="d-none" style="width: 100%; margin: 0 auto;">
                                            <div id="progress" class="card-body">
                                                <div class="progress">
                                                    <div id="progressBar"
                                                        class="progress-bar progress-bar-striped progress-bar-animated bg-dark"
                                                        role="progressbar" aria-valuenow="75" aria-valuemin="0"
                                                        aria-valuemax="100" style="width: 0%"></div>
                                                </div>
                                            </div>
                                        </div>

                                        <div style="margin-top: 2em">
                                            <div id="attendanceReportsCard" class="card border-dark d-none">
                                                <div class="card-header bg-dark p-1">
                                                    <h6 class="modal-title text-white font-weight-bold m-0">
                                                        Attendance Reports
                                                    </h6>
                                                </div>
                                                <div id="noAttendanceRecords" class="card-body d-none">
                                                    <h6 class="text-danger text-center font-weight-bolder">
                                                        No Attendance Records found.
                                                    </h6>
                                                </div>

                                                <div id="renderAttendanceReportsRange" class="card-body d-none"
                                                    style="margin-top: 1em;">

                                                </div>
                                            </div>
                                        </div>
                                    </div>
        </body>

        </html>