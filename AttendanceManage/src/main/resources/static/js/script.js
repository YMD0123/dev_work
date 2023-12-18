// 現在時間表示
timerID = setInterval('clock()',500);
function clock() {
    document.getElementById("view_clock").innerHTML = getNow();
}
function getNow() {
    var now = new Date();
    var year = now.getFullYear();
    var mon = now.getMonth()+1;
    var day = now.getDate();
    var hour = now.getHours();
    var min = now.getMinutes();
    var sec = now.getSeconds();

    var s = year + "/" + mon + "/" + day + " " + hour.toString().padStart(1, "0") + ":"
    + min.toString().padStart(2, "0") + ":" + sec.toString().padStart(2, "0");
    return s;
}

// アラート処理
function  atwork() {
    alert("出勤しますか？");
}

function  lework() {
    alert("退勤しますか？");
}

function  start() {
    alert("休憩を開始しますか？");
}

function  end() {
    alert("休憩を終了しますか？");
}

daysID = setInterval('days()',500);
function days() {
    document.getElementById("view_days").innerHTML = getToday();
}
function getToday() {

    const today = new Date();

    const year = today.getFullYear();
    const month = today.getMonth() + 1;
    const date = today.getDate();

    var days = year + "/" + month + "/" + date;

    return days;
}

document.addEventListener("DOMContentLoaded", function () {
    buttonColor(status);
});

function buttonColor() {
    var clockingIn = document.getElementById('clockingIn');
        var endBreak = document.getElementById('endBreak');

    if (status == '出勤中') {
        clockingIn.style.backgroundColor = '#EEEEEE';
        endBreak.style.backgroundColor = '#EEEEEE';
    } else if (status == '未出勤') {
        clockingOut.style.backgroundColor = '#EEEEEE';
        startBreak.style.backgroundColor = '#EEEEEE';
        endBreak.style.backgroundColor = '#EEEEEE';
    } else if (status == '休憩中') {
        clockingOut.style.backgroundColor = '#EEEEEE';
        startBreak.style.backgroundColor = '#EEEEEE';
        clockingIn.style.backgroundColor = '#EEEEEE';
    }
}