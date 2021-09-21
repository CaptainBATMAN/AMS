<%@ page language="java" contentType="text/html; charset = UTF-8" pageEncoding="UTF-8" %>
  <%@ page import="com.ams.*" %>

    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="UTF-8" />
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Student</title>
      <script src="../imported/jquery-3.6.0.js"></script>
      <script src="../imported/moment.js"></script>
      <!-- ! custom stylesheets -->
      <link rel="stylesheet" href="../custom_styles/studentHome.css" />

      <!-- ! bootstrap css and js -->
      <link rel="stylesheet" href="../imported/bootstrap.min.css" />
      <script src="../imported/bootstrap.4.4.1.min.js"></script>

      <!-- ! for Datepicker -->
      <link rel="stylesheet" href="../imported/datepicker.css" />
      <script src="../imported/bootstrap-datepicker.js"></script>

      <!-- ! for DataTables -->
      <link rel="stylesheet" type="text/css" href="./../DataTables/datatables.min.css" />
      <script type="text/javascript" src="./../DataTables/datatables.min.js"></script>
      <script src="./../imported/dataTables.buttons.min.js"></script>
      <script src="./../imported/buttons.print.min.js"></script>

      <!-- ! for alertify -->
      <link rel="stylesheet" href="../alertify/alertify.core.css" />
      <link rel="stylesheet" href="../alertify/alertify.default.css" id="linkID" />
      <script src="../alertify/alertify.min.js"></script>

      <!-- ! font awesome -->
      <script src="./../imported/font-awesome-5.15.4-all.js"></script>

      <!-- ! custom Scripts -->
      <script defer src="../custom_scripts/student_home.js"></script>
    </head>

    <body>
      <% response.setHeader("Cache-Control","no-cache, no-store,must-revalidate"); String UserEmail=(String)
        session.getAttribute("user"); String URL="" ; if(session.getAttribute("user")==null){ URL="/ams/login.jsp" ;
        response.sendRedirect(URL); } else if(!session.getAttribute("role").equals("student")){ URL="/ams/login.jsp" ;
        response.sendRedirect(URL); } %>

        <div id="nav-div">
          <nav class="p-1 navbar sticky-top navbar-expand-lg navbar-expand-md navbar-expand-sm navbar-dark"
            style="position: relative">
            <a class="navbar-brand p-0 pl-2" href="#">Gmeet AMS</a>
            <button class="p-2 navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
              aria-controls="navbarTogglerDemo01" aria-expanded="false" aria-label="Toggle navigation">
              <i class="fas fa-caret-down"></i>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
              <ul class="navbar-nav ml-auto mt-2 mt-lg-0 mt-sm-0 my-10">
                <li class="nav-item text-white mr-4" style="text-decoration: none;">
                  <a class="nav-link active" href="#">
                    <%= UserEmail%>
                  </a>
                </li>
                <li class="nav-item my-auto">
                  <form action="../logout" method="POST">
                    <button type="submit" id="logOutBtn" class="btn btn-outline-danger btn-sm m-0 m-auto"
                      style="border-radius: 4px" href="#">
                      <i class="fas fa-sign-out-alt"></i>&nbsp;&nbsp;Log Out
                    </button>
                  </form>
                </li>
              </ul>
            </div>
          </nav>
        </div>


        <div id="page-content-wrapper">
          <div class="container-fluid">

            <div id="view-attendance" class="">
              <h3>View Attendance Data</h3>
              <div class="form-group row">
                <div class="col-lg-6">
                  <label for="fromDate" class="p-0 col-form-label"> <span class="font-weight-bolder">From Date:</span>
                  </label>
                  <input type=" text " class="input-sm form-control form-control-sm" id="fromDate" style="margin-bottom: 10px;"/>
                </div>
                <div class="col-lg-6">
                  <label for="toDate" class="p-0  col-form-label"> <span class="font-weight-bolder">To Date:</span>
                  </label>
                  <input type="text " class="input-sm form-control form-control-sm" id="toDate" style="margin-bottom: 10px;"/>
                </div>
              </div>
              <button id="fetchButton" type="submit" style="cursor: pointer" class="btn btn-sm btn-outline-success">
                View
              </button>
            </div>

            <div id="progressBarCard" class="d-none" style="width: 80%; margin: 0 auto;">
              <div id="progress" class="card-body">
                <div class="progress">
                  <div id="progressBar" class="progress-bar progress-bar-striped progress-bar-animated bg-dark"
                    role="progressbar" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div>
                </div>
              </div>
            </div>

            <div style="padding: 3em 0;" id="attendanceReports">
              <div id="attendanceReportsCard" class="d-none">
                <div id="noAttendanceRecords" class="card-body d-none">
                  <h6 class="text-danger text-center font-weight-bolder">
                    No Attendance Records found.
                  </h6>
                </div>
                <div id="renderAttendanceReports" class="card-body d-none">
                  <table id="data-table" class="table table-bordered text-center table-condensed table-hover no-footer"
                    style="width: 100%; margin: 0 auto">
                    <thead>
                      <tr>
                        <th>Date</th>
                        <th>Period_1</th>
                        <th>Period_2</th>
                        <th>Period_3</th>
                      </tr>
                    </thead>
                  </table>
                </div>
              </div>
            </div>

          </div>
        </div>


    </body>

    </html>