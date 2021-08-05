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
      <link rel="stylesheet" href="../custom_styles/customStyles.css" />
      <script src="../custom_scripts/student_home.js"></script>
      <link rel="stylesheet" href="../imported/bootstrap.min.css" />
      <script src="../imported/bootstrap.4.4.1.min.js"></script>

      <!-- for Datepicker -->
      <link rel="stylesheet" href="../imported/datepicker.css" />
      <script src="../imported/bootstrap-datepicker.js"></script>

      <!-- for DataTables -->
      <link rel="stylesheet" type="text/css" href="./../DataTables/datatables.min.css" />
      <script type="text/javascript" src="./../DataTables/datatables.min.js"></script>
    </head>

    <body>
      <% response.setHeader("Cache-Control","no-cache, no-store,must-revalidate"); String UserEmail=(String)
        session.getAttribute("user"); String URL="" ; if(session.getAttribute("user")==null){ URL="/ams/login.jsp" ;
        response.sendRedirect(URL); } else if(!session.getAttribute("role").equals("student")){ URL="/ams/login.jsp" ;
        response.sendRedirect(URL); } %>

        <div class="div-center">
          <div id="nav-div" style="margin-bottom: 0em">
            <nav class="
            navbar
            sticky-top
            navbar-expand-lg navbar-dark
            bg-dark
            font-poppins
          " style="position: relative">
              <a class="navbar-brand p-0" href="./studentHome.html">AMS</a>
              <a class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span id="toggle-button" class="navbar-toggler-icon"></span>
              </a>
              <div class="navbar-collapse collapse" id="navbarNav">
                <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
                  <li class="nav-item active">
                    <a class="nav-link"><%= UserEmail%></a>
                </li>
                  <!-- <li class="nav-item active">
                    <a class="nav-link" style="
                    position: absolute;
                    right: 80px;
                    margin: auto 0;
                    bottom: 5px;
                  ">
                     
                    </a>
                  </li> -->
                  <li class="nav-item">
                    <form action="../logout" method="POST">
                      <button type="submit" id="logOutBtn" class="btn btn-danger btn-sm" href="#" style="
                      position: absolute;
                      right: 10px;
                      margin: auto 0;
                      bottom: 10px;
                    ">
                        Log Out
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
                  <div class="card-body">
                    <div class="form-row">
                      <div class="form-group col-md-6">
                        <p class="font-weight-bolder">
                          From Date:
                          <input type=" text " class="input-sm form-control" id="fromDate" />
                        </p>
                      </div>
                      <div class="form-group col-md-6">
                        <p class="font-weight-bolder">
                          To Date:
                          <input type="text " class="input-sm form-control" id="toDate" />
                        </p>
                      </div>
                    </div>
                    <button id="fetchButton" type="submit" style="cursor: pointer" class="btn btn-success btn-sm">
                      View
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div style="margin-top: 4em">
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

              <div id="renderAttendanceReports" class="card-body d-none">
                <table id="data-table" class="table table-bordered text-center" style="width: 100%; margin: 0 auto">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Period 1</th>
                      <th>Period 2</th>
                      <th>Period 3</th>
                    </tr>
                  </thead>
                </table>
              </div>
            </div>
          </div>
        </div>
    </body>

    </html>