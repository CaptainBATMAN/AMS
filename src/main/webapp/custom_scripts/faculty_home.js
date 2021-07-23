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
        if(!$("#attendanceReportsCard").hasClass("d-none")){
            $("#attendanceReportsCard").addClass("d-none");
        }
        if(!$("#noAttendanceRecords").hasClass("d-none")){
            $("#noAttendanceRecords").addClass("d-none");
        }
        if(!$("#renderAttendanceReports").hasClass("d-none")){
            $("#renderAttendanceReports").addClass("d-none");
        }
        renderAttendanceData();
    });

    
    function renderAttendanceData() {
        var meetingID = $("#gmeetcode").val();
    var date = $("#date").val();
    var dateInMillis = moment(date, 'YYYY-MM-DD').valueOf();
    var dateString = moment(dateInMillis, 'x').format('DD-MM-YYYY');
    console.log(dateString)

        $.ajax({
            url: '../facultyHomeAttendanceFetch',
            method: 'POST',
            data: { Meeting_ID : meetingID , Date : dateString},
            success: function (attendanceData) {
                if (!attendanceData.length) {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#noAttendanceRecords').removeClass('d-none');
                }
                else {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#renderAttendanceReports').removeClass('d-none');
                    console.dir(attendanceData)
                    $('#data-table').DataTable({
                        "retrieve": true,
                        "lengthMenu": [[5, 10, 25, 50, 75, 100, -1], [5, 10, 25, 50, 75, 100, "All"]],
                        "pageLength": 10,
                        "scrollX": true,
                        "dom": "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                        "data": attendanceData,
                        "columns": [
                            { "data": "Date" },
                            { "data": "Meeting_ID" },
                            { "data": "Participant_Email" },
                            { "data": "Duration" }
                        ]
                    });

                }
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });
    }

});




