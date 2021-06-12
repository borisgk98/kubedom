API_URL = "/api/kubedom"
AUTH_HEADER = "Authorization"

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

redirect = function (url) {
    window.location.href = url
}

log_error = (xhr, textStatus, jqXHR) => {
    console.log("ERROR")
    console.log(xhr.status);
}

logout = function () {
    const request_url = API_URL + "/logout"
    console.log(request_url)
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        'data': "{}",
        success: (data, textStatus, jqXHR) => {
            redirect("/signin");
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
    const request_url = API_URL + "/login"
    console.log(request_url)
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        data: JSON.stringify(data),
        'success': async () => {
            console.log("success")
            redirect("/homepage");
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
    const request_url = API_URL + "/register";
    console.log(request_url);
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        data: JSON.stringify(data),
        'success': async () => {
            console.log("success")
            redirect("/homepage");
        },
        error: log_error
    });
}

create_node = function () {
    var data = {
    };
    const request_url = API_URL + "/customer-node";
    console.log(request_url);
    jQuery.ajax({
        'type': 'POST',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        data: JSON.stringify(data),
        'success': async () => {
            console.log("success")
            redirect("/homepage");
        },
        error: log_error
    });
}

check_auth = function () {
    const request_url = API_URL + "/check";
    console.log(request_url);
    jQuery.ajax({
        'type': 'GET',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json',
        statusCode: {
            403: function() {
                redirect("/signin");
            },
            500: log_error
        }
    });
}