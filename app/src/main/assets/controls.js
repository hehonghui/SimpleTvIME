
function postKeyCode(keyCode) {
    $.post("/key", {
        code: keyCode
    },
    function(data) {
        console.log(data)
    })
}

$("#btnCls, #btnDel").on("click",
function() {
    postKeyCode($(this).attr("data-key"))
}),
$("#confirm").on("click",
function() {
    var $input = $("#inputarea"),
    text = $input.val();
    "" != text && ($input.val(""), $.post("/text", {
        text: text
    },
    function(data) {
        console.log(data)
    }))
})
