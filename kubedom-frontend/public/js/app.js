API_URL = "/api/kubedom"
AUTH_HEADER = "Authorization"

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

redirect = async function (url) {
    window.location.href = url
}

log_error = async (xhr, textStatus, jqXHR) => {
    console.log("ERROR")
    console.log(xhr.status);
}

logout = async function () {
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

signin_provider = async function () {
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

register_provider = async function () {
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

create_node = async function () {
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

check_auth = async function () {
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

async function load_homepage() {
    console.log("load_homepage()")
    const request_url = API_URL + "/app-user/current";
    console.log(request_url);
    let response = await jQuery.ajax({
        'type': 'GET',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json'
    });
    jQuery("#user-login").text(response['login'])
}

async function load_page(loader) {
    console.log("load_page start")
    await loader();
    console.log("loader done")
    jQuery("#spinner").hide()
    jQuery("#content").show()
}