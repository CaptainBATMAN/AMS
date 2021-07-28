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
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayHighlight: true
        });
        $(selector).datepicker('setDate', selectedDate);
    }

    $('#fetchButton').click(function () {
        if (!$("#attendanceReportsCard").hasClass("d-none")) {
            $("#attendanceReportsCard").addClass("d-none");
        }
        if (!$("#noAttendanceRecords").hasClass("d-none")) {
            $("#noAttendanceRecords").addClass("d-none");
        }
        if (!$("#renderAttendanceReports").hasClass("d-none")) {
            $("#renderAttendanceReports").addClass("d-none");
        }
        renderAttendanceData();
    });


    function renderAttendanceData() {
        var fromDate = $('#fromDate').val();
        var toDate = $('#toDate').val();
        var fromDateMillis = moment(fromDate, 'YYYY-MM-DD').valueOf();
        // var toDateMillis = moment(toDateString, 'YYYY-MM-DD').valueOf();
        // var totalNoOfDays = ((toDateMillis - fromDateMillis) / 86400000) + 1;
        // if (totalNoOfDays > 31) {
        //     alertify.error('Maximum range is 31 days. Please change time range and try again.', "6");
        //     return;
        // }
        // var noOfDaysCount = 1;
        // if (toDateMillis < fromDateMillis) {
        //     alertify.error('To Date is before From Date. Please select a valid date range.', "6");
        //     return;
        // }

        var currentDateString = moment(fromDateMillis, 'x').format('DD-MM-YYYY');

        $.ajax({
            url: '../fetchStudentAttendanceStudentHome',
            method: 'POST',
            data: { Date: currentDateString },
            success: function (attendanceData) {
                if (!attendanceData.length) {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#noAttendanceRecords').removeClass('d-none');
                }
                else {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#renderAttendanceReports').removeClass('d-none');
                    console.dir(attendanceData);

                    var date = attendanceData[0].date;
                    var P1 = attendanceData[0].P1.Class_Timings + "\n" + attendanceData[0].P1.Meeting_ID + "\n" + attendanceData[0].P1.Duration;
                    var P2 = attendanceData[0].P2.Class_Timings + "\n" + attendanceData[0].P2.Meeting_ID + "\n" + attendanceData[0].P2.Duration;
                    var P3 = attendanceData[0].P3.Class_Timings + "\n" + attendanceData[0].P3.Meeting_ID + "\n" + attendanceData[0].P3.Duration;
                   
                    console.log(P1)
                    console.log(P2)
                    console.log(P3)
                   
                   
                    var mainAttendanceData = [
                        {
                            "date": date,
                            "P1": P1,
                            "P2": P2,
                            "P3": P3
                        }
                ];
                    console.log(mainAttendanceData);
                    $('#data-table').DataTable({
                        "retrieve": true,
                        "lengthMenu": [[5, 10, 25, 50, 75, 100, -1], [5, 10, 25, 50, 75, 100, "All"]],
                        "pageLength": 10,
                        "scrollX": true,
                        "dom": "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                        "data": mainAttendanceData,
                        "columns": [
                            { "data": "date" },
                            { "data": "P1" },
                            { "data": "P2" },
                            { "data": "P3" }
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