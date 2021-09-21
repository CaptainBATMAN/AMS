$(document).ready(function () {
    var colorList = [
        '#7D1935',
        '#11052C',
        '#170055',
        '#012443',
        '#2D2424',
        '#000000',
        '#003638',
    ];
    var colorRand = Math.floor(Math.random() * 7);
    $('nav').css('background-color', colorList[colorRand]);
    $('th').css('background-color', colorList[colorRand]);
    $('th').addClass('text-white');

    var currentDate = new Date();
    initDates('fromDate', currentDate);
    initDates('toDate', currentDate);

    function initDates(elementId, selectedDate) {
        var selector = '#' + elementId;
        $(selector).datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayHighlight: true,
        });
        $(selector).datepicker('setDate', selectedDate);
    }

    $('#customTopicCheck').change(function () {
        if (this.checked) {
            $('#subject').attr('disabled', true);
            $('#subjectCard').addClass('d-none');
            $('#customTopicCard').removeClass('d-none');
        } else {
            $('#subject').removeAttr('disabled');
            $('#subjectCard').removeClass('d-none');
            $('#customTopicCard').addClass('d-none');
        }
    });

    var counter = 0;
    $('#fetchButton').click(function () {
        var fromDate = $('#fromDate').val();
        var toDate = $('#toDate').val();

        var className = $('#class').val();
        var subject = '';

        if (
            $('input[name="customTopicCheck"]').is(':checked') &&
            $('#customTopic').val() === ''
        ) {
            alertify.error('Please enter Custom Topic.');
            return;
        }

        if (
            $('input[name="customTopicCheck"]').is(':checked') &&
            !($('#customTopic').val() === '')
        ) {
            subject = $('#customTopic').val();
        } else {
            subject = $('#subject').val();
        }

        var fromDateMillis = moment(fromDate, 'YYYY-MM-DD').valueOf();
        var toDateMillis = moment(toDate, 'YYYY-MM-DD').valueOf();

        if (!$('#attendanceReportsCard').hasClass('d-none')) {
            $('#attendanceReportsCard').addClass('d-none');
        }
        if (!$('#noAttendanceRecords').hasClass('d-none')) {
            $('#noAttendanceRecords').addClass('d-none');
        }
        if (!$('#renderAttendanceReports').hasClass('d-none')) {
            $('#renderAttendanceReports').addClass('d-none');
        }
        if (!$('#renderAttendanceReportsRange').hasClass('d-none')) {
            $('#renderAttendanceReportsRange').addClass('d-none');
        }
        if (!$('#statsCard').hasClass('d-none')) {
            $('#statsCard').addClass('d-none');
        }

        var totalNoOfDays = (toDateMillis - fromDateMillis) / 86400000 + 1;
        if (totalNoOfDays > 31) {
            alertify.error(
                'Maximum range is 31 days. Please select a valid date range.'
            );
            return;
        }
        var noOfDaysCount = 1;
        if (toDateMillis < fromDateMillis) {
            alertify.error(
                'To Date is before From Date. Please select a valid date range.'
            );
            return;
        }

        if (totalNoOfDays === noOfDaysCount) {
            renderAttendanceDataForOne(fromDateMillis, subject, className);
        } else {
            renderAttendanceDataForRange(
                fromDateMillis,
                toDateMillis,
                subject,
                className
            );
        }
    });

    function renderAttendanceDataForOne(currentDateMillis, subject, className) {
        var currentDateString = moment(currentDateMillis, 'x').format(
            'DD-MM-YYYY'
        );
        $.ajax({
            url: '../facultyHomeAttendanceFetch',
            method: 'POST',
            data: {
                date: currentDateString,
                subject: subject,
                className: className,
            },
            success: function (attendanceData) {
                if (!attendanceData.length) {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#noAttendanceRecords').removeClass('d-none');
                } else {
                    $('#attendanceReportsCard').removeClass('d-none');
                    $('#renderAttendanceReports').removeClass('d-none');

                    // TODO Modify the attendanceData as per your requirement here

                    // ! Change minimum duration here..
                    var minDuration = 0;

                    var totalStudents = attendanceData.length;
                    var presentStudents = 0;
                    var absentStudents = 0;
                    for (let i = 0; i < attendanceData.length; i++) {
                        if (attendanceData[i].Duration > minDuration) {
                            attendanceData[i]['presentOrAbsent'] =
                                "<span class='text-success'>P</span>";
                            presentStudents += 1;
                        } else if (attendanceData[i].Duration === minDuration) {
                            attendanceData[i]['presentOrAbsent'] =
                                "<span class='text-danger'>A</span>";
                            absentStudents += 1;
                        }
                    }

                    $('#statsCard').removeClass('d-none');
                    const total = document.getElementById('totalStudents');
                    animateValue(total, 0, totalStudents, 1000);
                    const present = document.getElementById('presentStudents');
                    animateValue(present, 0, presentStudents, 1000);
                    const absent = document.getElementById('absentStudents');
                    animateValue(absent, 0, absentStudents, 1000);
                    renderTableForOne(attendanceData);
                }
            },
            error: function (jqXHR, exception) {
                console.log('Error occured while fetching data!!');
            },
        });
    }

    function renderAttendanceDataForRange(
        fromDateMillis,
        toDateMillis,
        subject,
        className
    ) {
        var totalNoOfDays = (toDateMillis - fromDateMillis) / 86400000 + 1;
        var noOfDaysCount = 1;
        var currentDateMillis = fromDateMillis;

        var mainAttendanceData = [];
        var availableDates = [];

        var newProgress = 0;
        $('#progressBarCard').removeClass('d-none');
        $('#progressBar').attr('aria-valuenow', 0).css('width', '0%');

        function getNextData() {
            var currentDateString = moment(currentDateMillis, 'x').format(
                'DD-MM-YYYY'
            );

            if (noOfDaysCount === totalNoOfDays) {
                $('#progressBar')
                    .attr('aria-valuenow', 100)
                    .css('width', '100%');
            } else {
                newProgress = (noOfDaysCount / totalNoOfDays) * 100;
                $('#progressBar')
                    .attr('aria-valuenow', newProgress)
                    .css('width', newProgress + '%');
            }

            main(currentDateString);
            async function main(currentDateString) {
                const key = currentDateString;
                const datax = await $.ajax({
                    url: '../facultyHomeAttendanceFetch',
                    method: 'POST',
                    data: {
                        date: currentDateString,
                        subject: subject,
                        className: className,
                    },
                    success: function (attendanceData) {
                        if (attendanceData.length) {
                            availableDates.push(key);
                            mainAttendanceData.push(attendanceData);
                        }
                    },
                    error: function (jqXHR, exception) {
                        console.log('Error occured while fetching data!!');
                    },
                });

                if (noOfDaysCount < totalNoOfDays) {
                    noOfDaysCount += 1;
                    currentDateMillis += 86400000;
                    getNextData();
                } else {
                    if (!mainAttendanceData.length) {
                        $('#progressBarCard').addClass('d-none');
                        $('#attendanceReportsCard').removeClass('d-none');
                        $('#noAttendanceRecords').removeClass('d-none');
                    } else {
                        var firstTime = true;

                        // ! Change minimum Duration Here
                        var minDuration = 0;

                        $('#progressBarCard').addClass('d-none');
                        $('#attendanceReportsCard').removeClass('d-none');
                        $('#renderAttendanceReportsRange').removeClass(
                            'd-none'
                        );

                        // ! Modify data here to get required data..

                        var emailList = [];
                        mainAttendanceData[0].forEach(function (key) {
                            emailList.push(key.Participant_Email);
                        });

                        var basicDataArray = [];
                        for (let i = 0; i < emailList.length; i++) {
                            var arr = [];
                            arr.push(emailList[i]);
                            basicDataArray.push(arr);
                        }

                        for (let i = 0; i < mainAttendanceData[0].length; i++) {
                            var data;
                            for (
                                let j = 0;
                                j < mainAttendanceData.length;
                                j++
                            ) {
                                var obj = mainAttendanceData[j][i];
                                var duration = obj.Duration;
                                if (duration > minDuration) {
                                    data =
                                        'Duration : ' +
                                        duration +
                                        "<br><span class='text-success'>P</span>";
                                } else {
                                    data =
                                        'Duration : ' +
                                        duration +
                                        "<br><span class='text-danger'>A</span>";
                                }
                                basicDataArray[i].push(data);
                            }
                        }

                        var $attendanceBody = $(
                            '#renderAttendanceReportsRange'
                        );
                        $attendanceBody.html('');
                        var tableStr =
                            "\
        <table id='attendanceReportsTable' class='table table-bordered text-center table-condensed table-hover no-footer' style='width: 100%; margin: 0 auto'>\
            <thead>\
                <tr>\
                    <th style='background-color:" +
                            colorList[colorRand] +
                            ";' class='text-white' >Email</th>";

                        for (let i = 0; i < availableDates.length; i++) {
                            tableStr +=
                                "<th style='background-color:" +
                                colorList[colorRand] +
                                ";' class='text-white' title='" +
                                availableDates[i] +
                                "'>" +
                                availableDates[i] +
                                '</th>';
                        }

                        tableStr +=
                            '</tr>\
                                </thead>\
                            </table>';

                        $attendanceBody.html(tableStr);

                        var table = $('#attendanceReportsTable').DataTable({
                            retrieve: true,
                            lengthMenu: [
                                [5, 10, 25, 50, 75, 100, -1],
                                [5, 10, 25, 50, 75, 100, 'All'],
                            ],
                            pageLength: 10,
                            scrollX: true,
                            dom: "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                            data: basicDataArray,
                        });
                    }
                }
            }
        }

        getNextData();
    }

    var table;
    function renderTableForOne(attendanceData) {
        if (counter >= 0) {
            if (counter > 0) {
                table.clear();
                table.rows.add(attendanceData).draw();
            } else {
                table = $('#data-table').DataTable({
                    retrieve: true,
                    lengthMenu: [
                        [5, 10, 25, 50, 75, 100, -1],
                        [5, 10, 25, 50, 75, 100, 'All'],
                    ],
                    pageLength: 10,
                    scrollX: true,
                    dom: "<'row'<'col-12 col-lg-2'l><'col-12 col-lg-6 text-center'B><'col-12 col-lg-4'f>><'row'<'col-12'tr>><'row'<'col-5'i><'col-7'p>>",
                    data: attendanceData,
                    columns: [
                        { data: null },
                        { data: 'Participant_Email' },
                        { data: 'Meeting_ID' },
                        { data: 'Class' },
                        { data: 'Subject' },
                        { data: 'Duration' },
                        { data: 'presentOrAbsent' },
                    ],
                    columnDefs: [
                        {
                            searchable: false,
                            orderable: false,
                            targets: 0,
                        },
                    ],
                    order: [[1, 'asc']],
                });
                table
                    .on('order.dt search.dt', function () {
                        table
                            .column(0, { search: 'applied', order: 'applied' })
                            .nodes()
                            .each(function (cell, i) {
                                cell.innerHTML = i + 1;
                            });
                    })
                    .draw();
                counter += 1;
            }
        }
    }

    function animateValue(obj, start, end, duration) {
        let startTimestamp = null;
        const step = (timestamp) => {
            if (!startTimestamp) startTimestamp = timestamp;
            const progress = Math.min(
                (timestamp - startTimestamp) / duration,
                1
            );
            obj.innerHTML = Math.floor(progress * (end - start) + start);
            if (progress < 1) {
                window.requestAnimationFrame(step);
            }
        };
        window.requestAnimationFrame(step);
    }
});
