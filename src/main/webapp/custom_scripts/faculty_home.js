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
            dateFormat: 'dd-mm-yy',
            autoclose: true,
            todayHighlight: true
        });
        $(selector).datepicker('setDate', selectedDate);
    }

    $('#fetchButton').click(function () {
        $('#attendanceReportsCard').removeClass('d-none');
    });


    function renderAttendanceData() {
        var name = $('#name').val();
        $.ajax({
            url: 'AjaxHandler',
            method: 'POST',
            data: { name: name },
            success: function (resultText) {
                $('#result').html(resultText);
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


