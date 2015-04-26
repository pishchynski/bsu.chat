
var username = null;
var messagesList = [];
var msgToEdit = null;

function run(){

    var allMessages = restore();
    createAllMessages(allMessages);
}

function createAllMessages(allMessages){
    for(var i = 0; i < allMessages.length; i++)
        addMsg(allMessages[i]);
}

function messageCreature(user, text){
    return{
        user: user,
        text: text,
        id: createId()
    };
}

function onSendButtonClick() {
    var msgField = document.getElementById('message-write-input');

    if(!window.username){
        alert("You must be logged in!");
        return;
    }

    if(!msgField.value)
    return;
    if(msgToEdit == null) {
        var message = messageCreature(username, msgField.value);
        addMsg(message);
    }
    else{
        editMsg(msgField.value);
        window.msgToEdit = null;
    }
}

function onSignInButtonClick(){
    var loginField = document.getElementById('login-input');
    logUserIn(loginField.value);
}

function logUserIn(value){
    if(!value) {
        alert("Input username!");
        return;
    }
    if(window.username){
        changeUserName(value);
        return;
    }
    window.username = value;
    var user = createNewUser(value);
    var users = document.getElementById('users');

    users.appendChild(user);

}

function changeUserName(name){
    var currentUserName = document.getElementById('yourName');
    var users = document.getElementById('users');
    users.removeChild(currentUserName);
    var user = createNewUser(name);
    users.appendChild(user);
    window.username = name;
}

function createNewUser(value){
    var divItem = document.createElement('div');

    divItem.classList.add('usr');
    divItem.setAttribute('id', 'yourName');
    divItem.appendChild(document.createTextNode(value));
    //var close = document.createElement('img');
    //close.src = "images/edit.png";
    //divItem.appendChild(close);

    return divItem;
}

function addMsg(message){

    messagesList.push(message);
    var item = createItem(message);
    var messages = document.getElementById('messages');
    messages.appendChild(item);

    store(messagesList);
}



function createId(){
    var id = Math.random();
    id *= Date.now();
    id = Math.floor(id);
    return id;
}

function createItem(msgStruct){
    var divItem = document.createElement('div');
    divItem.classList.add('msg');
    divItem.setAttribute('id', msgStruct.id.toString());
    var userNameSpan = divItem.appendChild(document.createElement('span'));
    userNameSpan.setAttribute('class', 'username');
    userNameSpan.appendChild(document.createTextNode(msgStruct.user.toString() + ': '));
    divItem.appendChild(userNameSpan);
    var paragraph = document.createElement('span');
    paragraph.setAttribute('class', 'msg-text');
    paragraph.appendChild(document.createTextNode(msgStruct.text));
    divItem.appendChild(paragraph);
    var edit = document.createElement('img');
    var close = document.createElement('img');
    edit.src = "images/edit.png";
    close.src = "images/close.png";
    edit.setAttribute("id", "edit-button");
    close.setAttribute("id", "remove-button");
    edit.setAttribute("onclick", "setMsgToEdit(this)");
    close.setAttribute("onclick", "removeMsg(this)");
    divItem.appendChild(close);
    divItem.appendChild(edit);
    return divItem;

}

function setMsgToEdit(item){
    window.msgToEdit = item.attributes['id'].value;
}

function editMsg(msgText){
    var message = document.getElementById(msgToEdit);
    message.getElementsByClassName("msg-text")[0].nodeValue = msgText;
}

function removeMsg(item){
    var parentItem = item.parentNode;
    (parentItem.parentNode).removeChild(parentItem);
    removeMsgFromLocal(item.parentNode);
    return;
}

function removeMsgFromLocal(item){
    var msgNum = item.attributes['id'].value;
    for(var i = 0; i < messagesList.length; ++i){
        if(msgNum == messagesList[i].id){
            messagesList.splice(i, 1);
            store(messagesList);
            break;
        }
    }
}

function store(listToSave) {

    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    localStorage.setItem("80Chat messages", JSON.stringify(listToSave));
}

function restore() {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }

    var item = localStorage.getItem("80Chat messages");

    return item && JSON.parse(item);
}