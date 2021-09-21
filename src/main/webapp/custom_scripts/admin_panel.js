$(document).ready(function () {

    var colorList = ['#7D1935', '#11052C', '#170055', '#012443', '#2D2424', '#000000', '#003638']
    var colorRand = Math.floor(Math.random() * 7);
    $('nav').css('background-color', colorList[colorRand]);
    $('#sidebar-wrapper').css('background-color', colorList[colorRand]);

    var sectionList = ["add-student", "add-faculty", "add-Class"]

    $("#menu-toggle").click(function (e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });

    $('.classList').select2({
        width: 'resolve'
    });
    $('.subjectList').select2();

    $('#addStudentBtn').click(function (e) {
        e.preventDefault();
        $('#dashboard').addClass('d-none');
        $('#add-faculty').addClass('d-none');
        $('#add-Class').addClass('d-none')
        $('#add-student').removeClass('d-none');
    });
    $('#addFacultyBtn').click(function (e) {
        e.preventDefault();
        $('#dashboard').addClass('d-none');
        $('#add-student').addClass('d-none');
        $('#add-Class').addClass('d-none')
        $('#add-faculty').removeClass('d-none');
    });
    $('#dashboardShow').click(function (e) {
        e.preventDefault();
        $('#add-student').addClass('d-none');
        $('#add-Class').addClass('d-none')
        $('#add-faculty').addClass('d-none');
        $('#dashboard').removeClass('d-none');
    });
    $('#addClassBtn').click(function (e) {
        e.preventDefault();
        $('#add-student').addClass('d-none');
        $('#add-faculty').addClass('d-none');
        $('#dashboard').addClass('d-none');
        $('#add-Class').removeClass('d-none')
    });



    $('#dashboardBtn').click(function (e) {
        e.preventDefault();
        if ($('#dashboard-content').hasClass('d-none')) {
            $('#dashboard-content').removeClass('d-none');
            $('#dashboardBtn').addClass('d-none');
            // $('#dashboardBtn').prop('disabled',true);
        }
        var dblistHtml = '';
        var dblist = [];
        $.ajax({
            url: '../getDBList',
            method: 'POST',
            success: function (response) {
                dblist = response;
                for (let i = 0; i < dblist.length; i++) {
                    var db = dblist[i];
                    dblistHtml += '<ul class="list-group">';
                    dblistHtml += '<li data-toggle="collapse" data-target="#db_' + i + '" class="dbName list-group-item col-lg-6 col-md-6 col-sm-12 text-white p-2"> ' + db[0] + '</li>';
                    dblistHtml += '<div class=" collapse" id="db_' + i + '">';
                    for (let j = 1; j < db.length; j++) {
                        dblistHtml += '<li class="collectionName list-group-item col-lg-6 col-md-6 col-sm-12 p-2"> ' + db[j] + '</li>';
                    }
                    dblistHtml += '</div></ul>';
                }
                $('#dashboard-content').html(dblistHtml);
                $('.dbName').css('background-color', colorList[colorRand]);
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });

    });

    $('#createStdntAccBtn').click(function (e) {
        e.preventDefault();
        var studentName = $('#studentName').val();
        var studentEmail = $('#studentEmail').val();
        var studentClass = $('#studentClass').val();
        var stuPasswd = $('#stuPasswd').val();
        if (studentName === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if (studentEmail === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if (stuPasswd === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        $.ajax({
            url: '../createStudentAccount',
            method: 'POST',
            data: { studentName: studentName, studentEmail: studentEmail, studentClass: studentClass, stuPasswd: stuPasswd },
            success: function (response) {
                $('#stdntAccCreationSuccessReport').removeClass('d-none');
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });
    });


    $('#createFacultyAccBtn').click(function (e) {
        e.preventDefault();
        var facultyName = $('#facultyName').val();
        var facultyEmail = $('#facultyEmail').val();
        var facultyClass = $('#facultyClass').val();
        var facultySubject = $('#facultySubject').val();
        var facPasswd = $('#facPasswd').val();



        if (facultyName === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if (facultyEmail === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if (facultyClass.length === 0) {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if (facultySubject.length === 0) {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if (facPasswd === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }

        var facClassString = ""
        facultyClass.forEach(element => {
            facClassString += element + " "
        });
        facClassString = facClassString.trimEnd();

        var facSubjectString = ""
        facultySubject.forEach(e => {
            facSubjectString += e + " ";
        })
        facSubjectString = facSubjectString.trimEnd();

        $.ajax({
            url: '../createFacultyAccount',
            method: 'POST',
            data: { facultyName: facultyName, facultyEmail: facultyEmail, facultyClass: facClassString, facultySubject: facSubjectString, facPasswd: facPasswd },
            success: function (response) {
                $('#facultyAccCreationSuccessReport').removeClass('d-none');
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });
    });

    $('#createClassBtn').click(function (e) {
        e.preventDefault();
        var className = $('#classNameToCreateClass').val()
        console.log(className)
        $.ajax({
            url: '../createClassDatabase',
            method: 'POST',
            data: { className : className },
            success: function (response) {
                if(response.msg === "Database Already Exists..."){
                    $('#classCreationSuccessReportText').removeClass("text-success");    
                    $('#classCreationSuccessReportText').addClass("text-danger");    
                    $('#classCreationSuccessReportText').html(response.msg);    
                }
                $('#classCreationSuccessReport').removeClass('d-none');
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });
    });


    $('#hasElectives').change(function (e) {
        e.preventDefault();
        if ($('#hasElectives').is(':checked')) {
            $('#subjectDataFile').prop('disabled', false);
        } else {
            $('#subjectDataFile').prop('disabled', true);
        }
    });

    $(document).on('keydown', function (event) {
        if (event.key == "Escape") {
            $("#wrapper").toggleClass("toggled");
        }
    });

    $("#showPwdFaculty").click(function (e) {
        e.preventDefault();
        if ($('#facPasswd').attr("type") == "password") {
            $('#facPasswd').attr("type", "text");
        } else {
            $('#facPasswd').attr("type", "password");
        }
    });

    $("#showPwdStudent").click(function (e) {
        e.preventDefault();
        if ($('#stuPasswd').attr("type") == "password") {
            $('#stuPasswd').attr("type", "text");
        } else {
            $('#stuPasswd').attr("type", "password");
        }
    });
});