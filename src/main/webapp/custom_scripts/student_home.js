$(document).ready(function () {

    var isMobile = /iPhone|iPad|iPod|Android/i.test(navigator.userAgent);
    if (isMobile) {
        $('.div-center').css("max-width", "100%");
    }

    var currentDate = new Date();
    initDates("fromDate", currentDate);
    initDates("toDate", currentDate);

    function initDates(elementId, selectedDate) {
        var selector = '#' + elementId;
        $(selector).datepicker({
            dateFormat: 'dd-mm-yy',
            autoclose: true,
            todayHighlight: true
        });
        $(selector).datepicker('setDate', selectedDate);
    }

    $('#fetchButton').click(renderAttendanceData);

    function renderAttendanceData() {
        var fromDate = $('#fromDate').val();
        var toDate = $('#toDate').val();
        $.ajax({
            url: '../fetchStudentAttendanceStudentHome',
            method: 'POST',
            data: { Date: '24-05-2021'},
            success: function (data) {
                if (data === null){
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#noAttendanceRecords').removeClass('d-none');
                    console.log("null")
                }
                else{
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#renderAttendanceReports').removeClass('d-none');
                    console.dir(data)
                    console.log(data)
                }
            },
            error: function (jqXHR, exception) {
                console.log('Error occured!!');
            }
        });
    }



});

function checkToggle() {
    if ($('#toggle-button').hasClass('bi-caret-down')) {
        $('#toggle-button').addClass('bi-caret-down-fill');
        $('#toggle-button').removeClass('bi-caret-down');
    } else {
        $('#toggle-button').removeClass('bi-caret-down-fill');
        $('#toggle-button').addClass('bi-caret-down');
    }
}