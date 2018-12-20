var currentId;
function setId(id){currentId = id;};

$(function () {
    $(":file").change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = imageIsLoaded;
            reader.readAsDataURL(this.files[0]);
        }
    });
});
function imageIsLoaded(e) {
    document.getElementById(currentId).src =e.target.result;
};

function createJson() {
    var theContent = $('#content');// set the content
    var editedContent = theContent.html();
    var json = {"content": editedContent};
    console.log(json);
    var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
    xmlhttp.open("POST", "sendmail");
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify(json));
}
/*
var theContent = $('#content2');// set the content

$('#preview').on('click', function () { //clear localstorage before showing preview
    if (localStorage.getItem('newContent')) {
        localStorage.clear();
    }
});

$('#save').on('click', function () { // store the new content in localStorage when the button is clicked
    var editedContent = theContent.html();
    localStorage.newContent = editedContent;
});
if (localStorage.getItem('newContent')) { // apply the newContent when it is exist ini localStorage
    theContent.html(localStorage.getItem('newContent'));
}

$('#restore').on('click', function () {
    if (localStorage.getItem('newContent')) { //clear localstorage to remove changes.
        localStorage.clear();
    }
});*/