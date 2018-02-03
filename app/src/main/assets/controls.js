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


function deleteOneChar() {
    var text = $("#inputarea").val().toString()
    if ( text.length > 0 ) {
        var newText = text.substring(0, text.length - 1)
        $("#inputarea").val(newText)
    }
}

function clearAll() {
    $("#inputarea").val('')
}

// delete one char
$("#btnDel").on("click",
function() {
    sendKeyCode($(this).attr("data-key"))
    deleteOneChar()
}),
// clear all
$("#btnCls").on("click",
function() {
    sendKeyCode($(this).attr("data-key"))
    clearAll()
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
    "" != text, $.get("/text?text=" + text, {
        text: text
    },
    function(data) {
        console.log(data)
    })
})


$(function(){
    $('.header-tab li').click(function() { 
         $('.header-tab li').removeClass("active");
         $(this).addClass("active");
         var pannel = $(this).data('tab');
         $('.panels > div').removeClass('active')
         $('#'+ pannel).addClass('active');
     });
 });