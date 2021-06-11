API_URL = "/api/kubedom"
PROVIDER_URL = "/provider"

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

redirect = function (url) {
    window.location.href = url
}

log_error = (data, textStatus, jqXHR) => {
    console.log("ERROR")
    console.log(data);
    console.log(textStatus);
    console.log(jqXHR);
}

logout = function () {
    const request_url = API_URL + PROVIDER_URL + "/logout"
    console.log(request_url)
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        'data': "{}",
        success: (data, textStatus, jqXHR) => {
            redirect("/");
        },
        error: log_error
    });
}

signin_provider = function () {
    var login = $("#provider_login").val();
    var password = $("#provider_password").val();
    var data = {
        login: login,
        password: password
    };
    const request_url = API_URL + PROVIDER_URL + "/login"
    console.log(request_url)
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        data: JSON.stringify(data),
        'success': async () => {
            console.log("success")
            redirect("/provider/homepage");
        },
        error: log_error
    });
}

register_provider = function () {
    var login = $("#provider_login").val();
    var password = $("#provider_password").val();
    var data = {
        login: login,
        password: password
    };
    const request_url = API_URL + PROVIDER_URL + "/register";
    console.log(request_url);
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        data: JSON.stringify(data),
        'success': async () => {
            console.log("success")
            redirect("/provider/homepage");
        },
        error: log_error
    });
}