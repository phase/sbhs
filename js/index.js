var url = "https://api.github.com/repos/phase/sbhs/git/refs/heads/master";
var request = new XMLHttpRequest();

request.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        var hash = JSON.parse(this.response).object.sha;
        document.getElementById("nightly").innerHTML = hash.substring(0, 7);
    }
};

request.open("GET", url);
request.send();