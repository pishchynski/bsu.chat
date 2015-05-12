
var username = null;
var messagesList = [];
var msgToEdit = null;

function run(){

    restore();
    //createAllMessages(allMessages);
}

function createAllMessages(allMessages){
    for(var i = 0; i < allMessages.length; i++)
        addMsgInternal(allMessages[i]);
}

function messageCreature(user, text){
    return{
        user: user,
        text: text
       // id: createId()
    };
}

var appState = {
    mainUrl : '80chat',
    msgList:[],
    token : 'TE11EN'
};

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

function addMsg(message, continueWith){
    post(appState.mainUrl, JSON.stringify(message), function(){
        restore();
    });
}

function addMsgInternal(message){

    //messagesList.push(message);
    var item = createItem(message);
    var messages = document.getElementById('messages');
    messages.appendChild(item);

    //store(messagesList);
}



/*function createId(){
    var id = Math.random();
    id *= Date.now();
    id = Math.floor(id);
    return id;
}*/

function createItem(msgStruct){
    var divItem = document.createElement('div');
    divItem.classList.add('msg');
    //divItem.setAttribute('id', msgStruct.id.toString());
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
    edit.src = "../resources/images/edit.png";
    close.src = "../resources/images/close.png";
    edit.setAttribute("id", "edit-button");
    close.setAttribute("id", "remove-button");
    edit.setAttribute("onclick", "setMsgToEdit(this.parentNode)");
    close.setAttribute("onclick", "removeMsg(this)");
    divItem.appendChild(close);
    divItem.appendChild(edit);
    return divItem;

}

function setMsgToEdit(item){
    window.msgToEdit = item;
}

function editMsg(msgText){
    var message = window.msgToEdit;
    var spanToDel = (message.getElementsByClassName("msg-text")).item(0);
    message.removeChild(spanToDel);
    var paragraph = document.createElement('span');
    paragraph.setAttribute('class', 'msg-text');
    paragraph.appendChild(document.createTextNode(msgText));
    message.appendChild(paragraph);
}

function removeMsg(item){
    var parentItem = item.parentNode;
    (parentItem.parentNode).removeChild(parentItem);
    removeMsgFromLocal(item.parentNode);
    return;
}

function restore(continueWith){
    var url = appState.mainUrl + '?token=' + appState.token;

    get(url, function(responseText) {
        console.assert(responseText != null);

        var response = JSON.parse(responseText);

        appState.token = response.token;

        createAllMessages(response.messages);
    }, function(message){
        defaultErrorHandler(message)
    });
}

function defaultErrorHandler(message) {
    console.error(message);
}

function get(url, continueWith, continueWithError) {
    ajax('GET', url, null, continueWith, continueWithError);
}

function post(url, data, continueWith, continueWithError) {
    ajax('POST', url, data, continueWith, continueWithError);
}

function put(url, data, continueWith, continueWithError) {
    ajax('PUT', url, data, continueWith, continueWithError);
}

function isError(text) {
    if(text == "")
        return false;

    try {
        var obj = JSON.parse(text);
    } catch(ex) {
        return true;
    }

    return !!obj.error;
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if(xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if(isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
    };

    xhr.ontimeout = function () {
        continueWithError('Server timed out !');
    }

    xhr.onerror = function (e) {
        var errMsg = 'Server connection error !\n'+
            '\n' +
            'Check if \n'+
            '- server is active\n'+
            '- server sends header "Access-Control-Allow-Origin:*"';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

window.onerror = function(err) {
}


/*function removeMsgFromLocal(item){
    var msgNum = item.attributes['id'].value;
    for(var i = 0; i < messagesList.length; ++i){
        if(msgNum == messagesList[i].id){
            messagesList.splice(i, 1);
            store(messagesList);
            break;
        }
    }
}*/

/*function store(listToSave) {

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
}*/