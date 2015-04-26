function resizeTextArea(){
    var back = document.getElementById("wrapper");
    var bHeight = back.offsetHeight;
    var header = document.getElementById("header");
    var hHeight = header.offsetHeight;
    var footer = document.getElementById("send-area");
    var fHeight = footer.offsetHeight;
    var main = document.getElementById("main-area");
    var height = bHeight - hHeight - fHeight - 12;
    var users = document.getElementById("users");
    main.style.height = height.toString()+'px';
    users.style.height = height.toString()+'px';


}
window.onresize = function(){resizeTextArea();};