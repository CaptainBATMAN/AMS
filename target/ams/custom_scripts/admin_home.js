$(document).ready(function () {

    var colorList = ['#7D1935', '#11052C', '#170055', '#012443', '#2D2424', '#000000', '#003638']
    var colorRand = Math.floor(Math.random() * 7);
    $('nav').css('background-color', colorList[colorRand]);
    $('#sidebar-wrapper').css('background-color', colorList[colorRand]);


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
        var fromDate = $("#fromDate").val();
        var toDate = $("#toDate").val();

        var className = $('#class').val();

        var fromDateMillis = moment(fromDate, 'YYYY-MM-DD').valueOf();
        var toDateMillis = moment(toDate, 'YYYY-MM-DD').valueOf();


        if (!$("#attendanceReportsCard").hasClass("d-none")) {
            $("#attendanceReportsCard").addClass("d-none");
        }
        if (!$("#noAttendanceRecords").hasClass("d-none")) {
            $("#noAttendanceRecords").addClass("d-none");
        }
        if (!$("#renderAttendanceReportsRange").hasClass("d-none")) {
            $("#renderAttendanceReportsRange").addClass("d-none");
        }
        if (!$("#xlsxDownloadButton").hasClass("d-none")) {
            $("#xlsxDownloadButton").addClass("d-none");
        }


        var totalNoOfDays = ((toDateMillis - fromDateMillis) / 86400000) + 1;
        if (totalNoOfDays > 31) {
            alertify.error('Maximum range is 31 days. Please select a valid date range.');
            return;
        }
        if (toDateMillis < fromDateMillis) {
            alertify.error('To Date is before From Date. Please select a valid date range.');
            return;
        }

        renderAttendanceDataForRange(fromDateMillis, toDateMillis, className);

    });


    function renderAttendanceDataForRange(fromDateMillis, toDateMillis, className) {
        var subjects = [];
        subjectList();
        async function subjectList() {
            const subData = $.ajax({
                url: '../getsubjectsAdminFetch',
                method: 'POST',
                data: { className: className },
                success: function (subjectList) {
                    for (let i = 0; i < subjectList.length; i++) {
                        subjects.push(subjectList[i]);
                    }
                },
                error: function (jqXHR, exception) {
                    console.log('Error occured while fetching data!!');
                }
            });

        }

        main();
        async function main() {

            const datax = await $.ajax({
                url: '../adminHomeFetch',
                method: 'POST',
                data: { fromDateMillis: fromDateMillis, toDateMillis: toDateMillis, className: className },
                success: function (attendanceData) {

                    if (attendanceData.length) {

                        console.log(attendanceData)
                        if (attendanceData[0].totalClasses === 0) {
                            $('#progressBarCard').addClass('d-none');
                            $('#attendanceReportsCard').removeClass('d-none');
                            $('#noAttendanceRecords').removeClass('d-none');
                        } else {

                            attendanceData.forEach(val => {
                                var totalclass = val.totalClasses;
                                var attendedClass = val.classesAttended;
                                var percentage = ((attendedClass / totalclass) * 100).toFixed(2);
                                val['percentage'] = percentage + "%";
                            });

                            console.log(attendanceData);
                            $('#progressBarCard').addClass('d-none');
                            $('#attendanceReportsCard').removeClass('d-none');
                            $('#renderAttendanceReportsRange').removeClass('d-none');


                            // ! Modify data here to get required data..

                            var $attendanceBody = $("#renderAttendanceReportsRange");
                            $attendanceBody.html("");
                            var tableStr = "\
                    <table id='attendanceReportsTable' class='table table-bordered text-center table-condensed table-hover no-footer' style='width: 100%; margin: 0 auto'>\
                        <thead>\
                            <tr>\
                                <th style='background-color:"+colorList[colorRand]+";' class='text-white'>Email</th>";

                            var columns = [{ "data": "Student_Email" }];

                            for (let i = 0; i < subjects.length; i++) {
                                tableStr += "<th style='background-color:"+colorList[colorRand]+";' class='text-white' title='" + subjects[i] + "'>" + subjects[i] + "</th>";
                                columns.push({ "data": subjects[i] });
                            }

                            tableStr += "<th style='background-color:"+colorList[colorRand]+";' class='text-white' title='" + "Classes Atttended" + "'>" + "Classes Attended" + "</th>";
                            tableStr += "<th style='background-color:"+colorList[colorRand]+";' class='text-white' title='" + "Total Classes" + "'>" + "Total Classes" + "</th>";
                            tableStr += "<th style='background-color:"+colorList[colorRand]+";' class='text-white' title='" + "Percentage" + "'>" + "Percentage" + "</th>";

                            columns.push({ "data": "classesAttended" });
                            columns.push({ "data": "totalClasses" });
                            columns.push({ "data": "percentage" });

                            tableStr += "</tr>\
                                            </thead>\
                                        </table>";


                            $attendanceBody.html(tableStr);

                            $('#attendanceReportsTable').DataTable({
                                "retrieve": true,
                                "lengthMenu": [[5, 10, 25, 50, 75, 100, -1], [5, 10, 25, 50, 75, 100, "All"]],
                                "pageLength": 10,
                                "scrollX": true,
                                "dom": "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                                "data": attendanceData,
                                "columns": columns,
                                "buttons": [{
                                    extend: 'excel',
                                    filename: "Attendance_" + className + "_" + $("#fromDate").val() + "_to_" + $("#toDate").val(),
                                    text: 'Download Excel',
                                    title: null,
                                    messageTop: "Attendance Data for " + className + ", from " + $("#fromDate").val() + " to " + $("#toDate").val() + " downloaded on " + new Date(),
                                    className: "btn btn-primary bg-primary btn-sm m-1",
                                }]
                            });
                        }

                    } else {
                        $('#progressBarCard').addClass('d-none');
                        $('#attendanceReportsCard').removeClass('d-none');
                        $('#noAttendanceRecords').removeClass('d-none');
                    }
                },
                error: function (jqXHR, exception) {
                    console.log('Error occured while fetching data!!');
                }
            });

        }
    }




});




