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
        var meetingID = $("#gmeetcode").val();
        var fromDate = $("#fromDate").val();
        var toDate = $("#toDate").val();

        var fromDateInMillis = moment(fromDate, 'YYYY-MM-DD').valueOf();
        var toDateInMillis = moment(toDate, 'YYYY-MM-DD').valueOf();
        var fromDateString = moment(fromDateInMillis, 'x').format('DD-MM-YYYY');
        var toDateString = moment(toDateInMillis, 'x').format('DD-MM-YYYY');

        var subject = $('#subject').val();
        var className = $('#class').val();

        $.ajax({
            url: '../facultyHomeAttendanceFetch',
            method: 'POST',
            data: { fromDate: fromDateString, toDate: toDateString, subject: subject, className: className, Meeting_ID: meetingID },
            success: function (attendanceData) {
                console.dir(attendanceData)
                if (!attendanceData.length) {
                    console.log('Has No records...')
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#noAttendanceRecords').removeClass('d-none');
                }
                else {
                    console.log('has records...')
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#renderAttendanceReports').removeClass('d-none');
                    console.dir(attendanceData)
                    var t = $('#data-table').DataTable({
                        "retrieve": true,
                        "lengthMenu": [[5, 10, 25, 50, 75, 100, -1], [5, 10, 25, 50, 75, 100, "All"]],
                        "pageLength": 10,
                        "scrollX": true,
                        "dom": "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                        "data": attendanceData,
                        "columns": [
                            { "data": null },
                            { "data": "Participant_Email" },
                            { "data": "Meeting_ID" },
                            { "data": "Class" },
                            { "data": "Subject" },
                            { "data": "Duration" }
                        ],
                        "columnDefs": [{
                            "searchable": false,
                            "orderable": false,
                            "targets": 0
                        }],
                        "order": [[1, 'asc']]
                    });
                    t.on('order.dt search.dt', function () {
                        t.column(0, { search: 'applied', order: 'applied' }).nodes().each(function (cell, i) {
                            cell.innerHTML = i + 1;
                        });
                    }).draw();
                }
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });
    }



});




