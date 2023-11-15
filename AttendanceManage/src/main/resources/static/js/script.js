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

    var s = year + "/" + mon + "/" + day + " " + hour + ":" + min + ":" + sec;
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
//    const week = today.getDay();

//    曜日表示用(実装無)
//    const weekItems = ["日", "月", "火", "水", "木", "金", "土"];
//    const day = weekItems[week];

    var days = year + "/" + month + "/" + date;

    return days;
}