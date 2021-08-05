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

    var counter = 0;
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
                if (!attendanceData.length) {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#noAttendanceRecords').removeClass('d-none');
                }
                else {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#renderAttendanceReports').removeClass('d-none');

                    // TODO Modify the attendanceData as per your requirement here

                    var totalStudents = attendanceData.length;
                    var presentStudents = 0;
                    var absentStudents = 0;
                    for (let i = 0; i < attendanceData.length; i++) {
                        if (attendanceData[i].Duration > 0) {
                            presentStudents += 1;
                        } else if (attendanceData[i].Duration === 0) {
                            absentStudents += 1;
                        }
                    }

                    $("#statsCard").removeClass("d-none");
                    const total = document.getElementById("totalStudents");
                    animateValue(total, 0, totalStudents, 1000);
                    const present = document.getElementById("presentStudents");
                    animateValue(present, 0, presentStudents, 1000);
                    const absent = document.getElementById("absentStudents");
                    animateValue(absent, 0, absentStudents, 1000);
                    console.dir(attendanceData);
                    renderTable(attendanceData);


                }
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            }
        });
    }

    var table;
    function renderTable(attendanceData) {
        if (counter >= 0) {
            if (counter > 0) {
                table.clear();
                table.rows.add(attendanceData).draw();
            }
            else {
                table = $('#data-table').DataTable({
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
                table.on('order.dt search.dt', function () {
                    table.column(0, { search: 'applied', order: 'applied' }).nodes().each(function (cell, i) {
                        cell.innerHTML = i + 1;
                    });
                }).draw();
                table.clear();
                counter += 1;
            }
        }
    }


    function animateValue(obj, start, end, duration) {
        let startTimestamp = null;
        const step = (timestamp) => {
            if (!startTimestamp) startTimestamp = timestamp;
            const progress = Math.min((timestamp - startTimestamp) / duration, 1);
            obj.innerHTML = Math.floor(progress * (end - start) + start);
            if (progress < 1) {
                window.requestAnimationFrame(step);
            }
        };
        window.requestAnimationFrame(step);
    }

});




