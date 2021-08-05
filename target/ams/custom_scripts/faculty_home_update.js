$(document).ready(function () {

    var isMobile = /iPhone|iPad|iPod|Android/i.test(navigator.userAgent);
    if (isMobile) {
        $('.div-center').css("max-width", "100%");
    }

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

    $('#fetchButton').click(function () {
        if ($('#gmeetcode').val() === "") {
            alert('Please fill all the fields before updating..');
            return;
        }
        if ($('#fromTime').val() === "") {
            alert('Please fill all the fields before updating..');
            return;
        }
        if ($('#toTime').val() === "") {
            alert('Please fill all the fields before updating..');
            return;
        }

        if (!$("#updateReportCard").hasClass("d-none")) {
            $("#updateReportCard").addClass("d-none");
        }
        
        renderAttendanceData();
    });


    function renderAttendanceData() {
        var meetingID = $("#gmeetcode").val();
        var date = $("#date").val();
        var dateInMillis = moment(date, 'YYYY-MM-DD').valueOf();
        var dateString = moment(dateInMillis, 'x').format('DD-MM-YYYY');
        var fromTime = ($('#fromTime').val());
        var toTime = ($('#toTime').val());
        var subject = $('#subject').val();
        var className = $('#class').val();
        var period = $('input[name="period"]:checked').val();

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




