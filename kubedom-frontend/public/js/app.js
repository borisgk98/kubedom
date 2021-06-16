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

async function curr_user() {
    const request_url = API_URL + "/app-user/current";
    let response = await jQuery.ajax({
        'type': 'GET',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json'
    });
    return response;
}

async function load_homepage() {
    console.log("load_homepage()")
    let response = await curr_user();
    jQuery("#user-login").text(response['login'])
    jQuery("#provider-token").text(response['token'])

    let myNodes = response['myNodes']
    myNodes.forEach(node => {
        jQuery("#my-provider-nodes").after("                    <tr>\n" +
            "                        <th scope=\"row\">" + node['id'] + "</th>\n" +
            "                        <td>" + node['externalIp'] + "</td>\n" +
            "                        <td>" + node['nodeUuid'] + "</td>\n" +
            "                        <td>" + node['type'] + "</td>\n" +
            "                        <td>" + node['providerNodeState'] + "</td>\n" +
            "                    </tr>")
    });

    let notMyNodes = response['notMyNodes']
    notMyNodes.forEach(node => {
        jQuery("#not-my-provider-nodes").after("                    <tr>\n" +
            "                        <th scope=\"row\">" + node['id'] + "</th>\n" +
            "                        <td>" + node['externalIp'] + "</td>\n" +
            "                        <td>" + node['nodeUuid'] + "</td>\n" +
            "                        <td>" + node['type'] + "</td>\n" +
            "                        <td>" + node['providerNodeState'] + "</td>\n" +
            "                    </tr>")
    });

    let clusters = response['clusters']
    clusters.forEach(cluster => {
        console.log(cluster)
        jQuery("#clusters").after("                    <tr>\n" +
            "                        <th scope=\"row\"><a href='/cluster/" + cluster['id'] + "'>" + cluster['id'] + "</a></th>\n" +
            "                        <td>" + cluster['nodes'].length + "</td>\n" +
            "                        <td>" + cluster['status'] + "</td>\n" +
            "                    </tr>")
    });
}

async function load_cluster() {
    let url = window.location.href;
    let parts = url.split('/');
    let clusterId = parts.pop() || parts.pop();
    const request_url = API_URL + "/kube-cluster/" + clusterId;
    let cluster = await jQuery.ajax({
        'type': 'GET',
        'url': request_url,
        'contentType': 'application/json',
        'dataType': 'json'
    });

    jQuery("#cluster-name").text("Кластер " + clusterId)

    let masterNode = null
    cluster.nodes.forEach(node => {
        if (node.kubectlConfig != null && node['type'] == 'MASTER') {
            masterNode = node
        }
    })
    if (masterNode != null) {
        jQuery("#kubectl-config").text(masterNode.kubectlConfig)
    }

    cluster.nodes.forEach(node => {
        jQuery("#cluster-nodes").after("                    <tr>\n" +
            "                        <th scope=\"row\">" + node['id'] + "</th>\n" +
            "                        <td>" + node['type'] + "</td>\n" +
            "                        <td>" + node['customerNodeState'] + "</td>\n" +
            "                        <td>" + node['providerNodeId'] + "</td>\n" +
            "                        <td>" + node['ready'] + "</td>\n" +
            "                    </tr>")
    });
}

async function load_page(loader) {
    console.log("load_page start")
    await loader();
    console.log("loader done")
    jQuery("#spinner").hide()
    jQuery("#content").show()
}