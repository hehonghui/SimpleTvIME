var isSupportTouch = "ontouchend" in document ? true: false;

function postKeyCode(keyCode) {
    $.post("/key", {
        code: keyCode
    },
    function(data) {
        console.log(data)
    })
}

function sendKeyCode(keyCode) {
    $.get("/key?keycode=" + keyCode, {
        code: keyCode
    },
    function(data) {
        console.log(data)
    })
}


$("#btnCls, #btnDel").on("click",
function() {
//    postKeyCode($(this).attr("data-key"))
    sendKeyCode($(this).attr("data-key"))
}),
$(".direction").on(isSupportTouch ? "touchstart": "mousedown",
function() {
    var o = $(this);
    $("#direction-btns").css({
        "background-position": o.attr("data-bp")
    }),
    sendKeyCode(o.attr("data-key"))
}),
$(".otherbtn").on(isSupportTouch ? "touchstart": "mousedown",
function() {
    var o = $(this);
    o.css({
        "background-position": o.attr("data-bp")
    }),
    sendKeyCode(o.attr("data-key"))
}),
$(".direction,.otherbtn").on(isSupportTouch ? "touchend touchmove": "mouseup mousemove",
function() {
    $("#direction-btns,.direction,.otherbtn").css({
        "background-position": ""
    })
}),
$("#confirm").on("click",
function() {
    var $input = $("#inputarea"),
    text = $input.val();
    console.log('input : ' + text)
    //post 请求被pending了,不知道为什么, 先测试 get
//    "" != text && ($input.val(""), $.post("/text", {
//        text: text
//    },
    "" != text && ($input.val(""), $.get("/text?text=" + text, {
        text: text
    },
    function(data) {
        console.log(data)
    }))
})
