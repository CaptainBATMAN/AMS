<%@ page language="java" contentType="text/html; charset = UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.ams.*,java.util.*" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <meta http-equiv="content-type" content="text/plain; charset=UTF-8" />
            <title>Admin</title>
            <script src="../imported/jquery-3.6.0.js"></script>
            <script src="../imported/moment.js"></script>

            <!-- ! custom stylesheets -->
            <link rel="stylesheet" href="../custom_styles/adminPanel.css" />
            
            <!-- ! bootstrap css and js -->
            <link rel="stylesheet" href="../imported/bootstrap.min.css" />
            <script src="../imported/bootstrap.4.4.1.min.js"></script>
            
            <!-- ! for Datepicker -->
            <link rel="stylesheet" href="../imported/datepicker.css" />
            <script src="../imported/bootstrap-datepicker.js"></script>
          
            <!-- ! for alertify -->
            <link rel="stylesheet" href="../alertify/alertify.core.css" />
            <link rel="stylesheet" href="../alertify/alertify.default.css" id="linkID" />
            <script src="../alertify/alertify.min.js"></script>
            
            <!-- ! font awesome -->
            <script src="./../imported/font-awesome-5.15.4-all.js"></script>
            
            <!-- ! select2 -->
            <link rel="stylesheet" href="./../imported/select2.min.css">
            <script src="./../imported/select2.min.js"></script>

            <!-- ! custom Scripts -->
            <script src="../custom_scripts/admin_panel.js"></script>
        </head>
        
        <body>
            <% response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); String URL="" ;
                if(session.getAttribute("user")==null){ URL="/ams/login.jsp" ; response.sendRedirect(URL); } else
                if(!session.getAttribute("role").equals("admin")){ URL="/ams/login.jsp" ; response.sendRedirect(URL); }
                else{ 
                    ArrayList<String> classes = new ArrayList<String>();
                    classes = (ArrayList<String>) session.getAttribute("class");
                 %>

                <div id="nav-div" style="margin-bottom: 0em">
                    <nav class="
        navbar
        sticky-top
        navbar-expand-lg 
        navbar-expand-sm
        navbar-dark" style="position: relative;">
                        <a class="navbar-brand p-0" href="#">Gmeet AMS</a>
                        <div class="navbar-collapse" id="navbarNav">
                            <ul class="navbar-nav mr-auto mt-2 mt-lg-0 mt-sm-0">
                                <li class="nav-item">
                                    <button href="#menu-toggle" class="btn btn-outline-light btn-sm" id="menu-toggle"
                                        data-toggle="tooltip" title="Use Esc to toggle"><i
                                            class="fas fa-chevron-left"></i>
                                    </button>
                                </li>
                                <li class="nav-item text-white">
                                    
                                </li>
                            </ul>
                        </div>
                    </nav>
                </div>


                <div id="wrapper">

                    <!-- Sidebar -->
                    <div id="sidebar-wrapper">
                        <ul class="sidebar-nav">
                            <li>
                                <a href="./adminHome.jsp"><i class="fas fa-chart-line"></i>&nbsp;&nbsp;Fetch Attendance</a>
                            </li>
                            <li>
                                <a id="dashboardShow" href="#"><i class="fas fa-chart-line"></i>&nbsp;&nbsp;Dashboard</a>
                            </li>
                            <li>
                                <a id="addStudentBtn" href="#"><i class="fas fa-address-card"></i>&nbsp;&nbsp;Add Student</a>
                            </li>
                            <li>    
                                <a id="addFacultyBtn" href="#"><i class="fas fa-address-card"></i>&nbsp;&nbsp;Add Faculty</a>
                            </li>
                            <li>
                                <a id="addClassBtn" href="#"><i class="fas fa-plus"></i>&nbsp;&nbsp;Add Class</a>
                            </li>
                            <li>
                                <a href="#" style="border-radius: 0;"><i class="fas fa-database"></i>&nbsp;&nbsp;Mongo
                                    Atlas</a>
                            </li>
                            <li>
                                <form action="../logout" method="POST">
                                    <button type="submit" id="logOutBtn"
                                        class="btn btn-danger btn-block btn-md navbar-right" style="border-radius: 0;"
                                        href="#">
                                        <i class="fas fa-sign-out-alt"></i>&nbsp;&nbsp;Log Out
                                    </button>
                                </form>
                            </li>
                        </ul>
                    </div>
                    <!-- /#sidebar-wrapper -->

                    <!-- Page Content -->
                    <div id="page-content-wrapper">
                        <div class="container-fluid">

                            <!-- dashboard -->
                            <div id="dashboard" class="">
                                <button id="dashboardBtn" class="btn btn-md btn-outline-dark btn-block">Show
                                    Databases</button>
                                <div id="dashboard-content" class="continer-fluid d-none">

                                </div>
                            </div>
                            <!-- /dashboard -->

                            <!-- add Student -->
                            <div id="add-student" class="d-none">
                                <h3 class="text-primary">Add Student</h3>
                                <small class="form-text text-danger">* All fields are required.</small>

                                <form>
                                    <div class="form-group row">
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="studentName" class="p-0 col-sm-2 col-form-label"> <span
                                                    class="font-weight-bolder">Name</span>
                                            </label>
                                            <input id="studentName" type="text" class="form-control form-control-sm">
                                        </div>
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="studentEmail" class="p-0 col-sm-2 col-form-label"> <span
                                                    class="font-weight-bolder">Email</span>
                                            </label>
                                            <input id="studentEmail" type="email" class="form-control form-control-sm">
                                        </div>
                                    </div>
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="studentClass" class="p-0 col-form-label"> <span
                                                    class="font-weight-bolder">
                                                    Class</span>
                                            </label>
                                            <select id="studentClass" class="classList form-control"
                                                style="width: 100%;" name="class">
                                                <% for(int i=0; i < classes.size();i++) {%>
                                                                    <option class="custom-options"
                                                                        value="<%= classes.get(i)%>">
                                                                        <%= classes.get(i)%>
                                                                    </option>
                                                                    <% } %>
                                            </select>
                                        </div>
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="stuPasswd" class="p-0 col-form-label"><span
                                                    class="font-weight-bolder">
                                                    Password</span></label>
                                            <input id="stuPasswd" class="form-control form-control-sm" type="password">
                                            <i id="showPwdStudent" class="toggle-password">Show</i>
                                        </div>

                                    </div>
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <button id="createStdntAccBtn" class="btn btn-sm btn-outline-primary">Create Account</button>
                                        </div>
                                    </div>
                                </form>
                                <div id="stdntAccCreationSuccessReport" style="width: 80%; margin: 0 auto;" class="d-none">
                                    <h3 style="font-family: Montserrat;" class="text-center text-success">Account Created Successfully.</h3>
                                </div>
                            </div>
                            <!-- /add Student -->

                            <!-- add faculty -->
                            <div id="add-faculty" class="d-none">
                                <h3 class="text-primary">Add Faculty</h3>
                                <small class="form-text text-danger">* All fields are required.</small>

                                <form>
                                    <div class="form-group row">
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="facultyName" class="p-0 col-sm-2 col-form-label"> <span
                                                    class="font-weight-bolder">Name</span>
                                            </label>
                                            <input id="facultyName" type="text" class="form-control form-control-sm">
                                        </div>
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="facultyEmail" class="p-0 col-sm-2 col-form-label"> <span
                                                    class="font-weight-bolder">Email</span>
                                            </label>
                                            <input id="facultyEmail" type="email" class="form-control form-control-sm">
                                        </div>
                                    </div>
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="facultyClass" class="col-sm-12 p-0 col-form-label"> <span
                                                    class="font-weight-bolder">
                                                    Class</span>
                                            </label>
                                            <select id="facultyClass" class=" classList form-control" name="class[]"
                                                style="width: 100%;" multiple="multiple">
                                                <% for(int i=0; i < classes.size();i++) {%>
                                                                    <option class="custom-options"
                                                                        value="<%= classes.get(i)%>">
                                                                        <%= classes.get(i)%>
                                                                    </option>
                                                                    <% } %>
                                                                    <% } %>
                                            </select>
                                        </div>
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="facultySubject" class="col-sm-12 p-0 col-form-label"> <span
                                                    class="font-weight-bolder">
                                                    Subjects</span>
                                            </label>
                                            <select id="facultySubject" class="subjectList form-control"
                                                style="width: 100%;" name="subject[]" multiple="multiple">
                                                <option>AI</option>
                                                <option>ML</option>
                                                <option>USP</option>
                                                <option>ST</option>
                                                <option>CD</option>
                                                <option>C</option>
                                                <option>CNS</option>
                                                <option>OOPS</option>
                                                <option>MAD</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="facPasswd" class="p-0 col-form-label"><span
                                                    class="font-weight-bolder">
                                                    Password</span></label>
                                            <input id="facPasswd" class="form-control form-control-sm" type="password" />
                                            <i id="showPwdFaculty" class="toggle-password">Show</i>
                                            </div>
                                    </div>
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <button id="createFacultyAccBtn" class="btn btn-sm btn-outline-primary">Create Account</button>
                                        </div>
                                    </div>
                                </form>
                                <div id="facultyAccCreationSuccessReport" style="width: 80%; margin: 0 auto;" class="d-none">
                                    <h3 style="font-family: Montserrat;" class="text-center text-success">Account Created Successfully.</h3>
                                </div>
                            </div>
                            <!-- add faculty -->

                            <!-- add Class-->
                            <div id="add-Class" class="d-none">
                                <h3 class="text-primary">Add Class</h3>
                                <small class="form-text text-danger">* All fields are required.</small>
                                
                                <form>
                                    <div class="form-group row">
                                        <div class="col-lg-6 col-sm-12">
                                            <label class="p-0 col-sm-4 col-form-label"> <span class="font-weight-bolder">Download
                                                    Templates: </span>
                                            </label>
                                            <a class="btn btn-sm btn-outline-dark"
                                                href="./../templates/student_data_template_for_class_registration.csv"
                                                download="StudentDataTemplateForClassRegistration.csv"><i
                                                    class="fa fa-download"></i> Student Data Template</a>
                                            <a class="btn btn-sm btn-outline-dark"
                                                href="./../templates/subject_template_for_class_registration.csv"
                                                download="SubjectTemplateForClassRegistration.csv"><i class="fa fa-download"></i>
                                                Subjects Template</a>
                                                <small class="form-text text-danger">* Please note that the student details has to be added to the respective class databases.</small>
                                        </div>
                                    
                                    </div>
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <label for="classNameToCreateClass" class="p-0 col-sm-2 col-form-label"> <span
                                                    class="font-weight-bolder">Class :</span>
                                            </label>
                                            <input id="classNameToCreateClass" type="text" class="form-control form-control-sm" required>
                                            <small class="form-text text-danger">* Class Name must be like [Dept-YearSection] Ex:
                                                CSE-1A </small>
                                        </div>
                                    </div>
                                
                                    <div class="form-group row" style="align-items: center;">
                                        <div class="col-lg-6 col-sm-12">
                                            <button class="btn btn-sm btn-outline-primary" id="createClassBtn">Create Class</button>
                                        </div>
                                    </div>
                                </form>
                                  <div id="classCreationSuccessReport" style="width: 80%; margin: 0 auto;" class="d-none">
                                      <h3  id="classCreationSuccessReportText" style="font-family: Montserrat;" class="text-center text-success">Class Created Successfully.</h3>
                                </div>
                            </div>
                            <!-- /add class -->
                        </div>
                    </div>
                    <!-- /#page-content-wrapper -->
                </div>
                <!-- /#wrapper -->
        </body>

        </html>