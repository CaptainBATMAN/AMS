$(document).ready(function () {

    var colorList = ['#7D1935', '#11052C', '#170055', '#012443', '#2D2424', '#000000', '#003638']
    var colorRand = Math.floor(Math.random() * 7);
    $('nav').css('background-color', colorList[colorRand]);
    $('th').css('background-color', colorList[colorRand]);
    $('th').addClass('text-white');

    var currentDate = new Date();
    initDates("date", currentDate);

    function initDates(elementId, selectedDate) {
        var selector = '#' + elementId;
        $(selector).datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayHighlight: true
        });
        $(selector).datepicker('setDate', selectedDate);
    }

    $('#customTopicCheck').change(function () {
        if (this.checked) {
            $("#subject").attr("disabled", true);
            $("#subjectCard").addClass("d-none");
            $("#customTopicCard").removeClass("d-none");
        } else {
            $("#subject").removeAttr("disabled");
            $("#subjectCard").removeClass("d-none");
            $("#customTopicCard").addClass("d-none");
        }
    });

    $('#updateBtn').click(function () {

        if (!$("#updateReportCard").hasClass("d-none")) {
            $("#updateReportCard").addClass("d-none");
        }
        if ($('#gmeetcode').val() === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if ($('#fromTime').val() === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if ($('#toTime').val() === "") {
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if($('input[name="period"]:checked').val() === undefined){
            alertify.error('Fill all the fields before updating..');
            return;
        }
        if ($('input[name="customTopicCheck"]').is(":checked") && ($('#customTopic').val() === "")) {
            alertify.error('Please enter Custom Topic.');
            return;
         }
        
        renderAttendanceData();
    });


    function renderAttendanceData() {
        var meetingID = $("#gmeetcode").val().toUpperCase();
        var date = $("#date").val();
        var dateInMillis = moment(date, 'YYYY-MM-DD').valueOf();
        var dateString = moment(dateInMillis, 'x').format('DD-MM-YYYY');
        var fromTime = ($('#fromTime').val());
        var toTime = ($('#toTime').val());
        var className = $('#class').val();
        var period = $('input[name="period"]:checked').val();

        var subject = "";
        if ($('input[name="customTopicCheck"]').is(":checked") && !($('#customTopic').val() === "")) {
            subject = $('#customTopic').val();
        }
        else {
            subject = $('#subject').val();
        }

        $.ajax({
            url: '../facultyHomeAttendanceUpdate',
            method: 'POST',
            data: { Meeting_ID: meetingID, Date: dateString, fromTime: fromTime, toTime: toTime, subject: subject, className: className, period: period },
            success: function (data) {
                var msg = data.msg;
                $('#updateReport').addClass("text-success");
                $('#updateReport').addClass("text-center");
                $('#updateReport').html(msg);
                $('#updateReportCard').removeClass("d-none");
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
                $('#updateReport').addClass("text-danger");
                $('#updateReport').addClass("text-center");
                $('#updateReport').html("Unable to Update Student Records..");
                $('#updateReportCard').removeClass("d-none");
            }
        });
    }

});




