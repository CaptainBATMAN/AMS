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
        var fromDate = $('#fromDate').val();
        var toDate = $('#toDate').val();
        var fromDateMillis = moment(fromDate, 'YYYY-MM-DD').valueOf();
        var toDateMillis = moment(toDate, 'YYYY-MM-DD').valueOf();

        var totalNoOfDays = ((toDateMillis - fromDateMillis) / 86400000) + 1;

        if (totalNoOfDays > 31) {
            alert('Maximum range is 31 days. Please change time range and try again.');
            return;
        }
        var noOfDaysCount = 1;
        if (toDateMillis < fromDateMillis) {
            alert('To Date is before From Date. Please select a valid date range.');
            return;
        }

        var currentDateMillis = fromDateMillis;

        var mainAttendanceData = [];
        function getNextData() {
            var currentDateString = moment(currentDateMillis, 'x').format('DD-MM-YYYY');

            main(currentDateString);
            async function main(currentDateString) {

                const data = await $.ajax({
                    url: '../fetchStudentAttendanceStudentHome',
                    method: 'POST',
                    data: { Date: currentDateString },
                    success: function (attendanceData) {
                        if (attendanceData.length) {
                            mainAttendanceData.push(attendanceData[0]);
                        }
                    },
                    error: function (jqXHR, exception) {
                        console.log('Error occured while fetching data!!');
                    }
                });

                if (noOfDaysCount < totalNoOfDays) {
                    noOfDaysCount += 1;
                    currentDateMillis += 86400000;
                    getNextData();
                } else {
                    var finalMainAttendanceData = [];
                    if (!mainAttendanceData.length) {
                        $('#attendanceReportsCard').removeClass('d-none');
                        $('#noAttendanceRecords').removeClass('d-none');
                    }
                    else {
                        $('#attendanceReportsCard').removeClass('d-none');
                        $('#renderAttendanceReports').removeClass('d-none');

                        

                        for (let i = 0; i < mainAttendanceData.length; i++) {


                            var date = mainAttendanceData[i].date;

                            var P1 = mainAttendanceData[i].P1.Subject + "<span>&nbsp&nbsp&nbsp</span>" + mainAttendanceData[i].P1.Class_Timings + "<br>" + "Duration: " + mainAttendanceData[i].P1.Duration;
                            var P2 = mainAttendanceData[i].P2.Subject + "<span>&nbsp&nbsp&nbsp</span>" + mainAttendanceData[i].P2.Class_Timings + "<br>" + "Duration: " + mainAttendanceData[i].P2.Duration;
                            var P3 = mainAttendanceData[i].P3.Subject + "<span>&nbsp&nbsp&nbsp</span>" + mainAttendanceData[i].P3.Class_Timings + "<br>" + "Duration: " + mainAttendanceData[i].P3.Duration;

                            P1 = P1 + ((mainAttendanceData[i].P1.Duration > 0) ? "<br><span class='text-success'>P</span>" : "<br><span class='text-danger'>A</span>");
                            P2 = P2 + ((mainAttendanceData[i].P2.Duration > 0) ? "<br><span class='text-success'>P</span>" : "<br><span class='text-danger'>A</span>");
                            P3 = P3 + ((mainAttendanceData[i].P3.Duration > 0) ? "<br><span class='text-success'>P</span>" : "<br><span class='text-danger'>A</span>");

                            finalMainAttendanceDataArrayObj =
                            {
                                "date": date,
                                "P1": P1,
                                "P2": P2,
                                "P3": P3
                            };
                            finalMainAttendanceData.push(finalMainAttendanceDataArrayObj);
                        }
                    }
                    if(finalMainAttendanceData.length){
                        renderTable(finalMainAttendanceData);
                    }
                }
            }
        }

        getNextData();
    }

    var table;
    function renderTable(finalMainAttendanceData) {

        if (counter >= 0) {
            if (counter > 0) {
                table.clear();
                table.rows.add(finalMainAttendanceData).draw();
            }
            else {
                table = $('#data-table').DataTable({
                    "retrieve": true,
                    "lengthMenu": [[5, 10, 25, 50, 75, 100, -1], [5, 10, 25, 50, 75, 100, "All"]],
                    "pageLength": 10,
                    "scrollX": true,
                    "dom": "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                    "data": finalMainAttendanceData,
                    "columns": [
                        { "data": "date" },
                        { "data": "P1" },
                        { "data": "P2" },
                        { "data": "P3" }
                    ]
                });
                table.clear();
                counter += 1;
            }
        }


    }
});