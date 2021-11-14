function successNotification(message) {
    createNotification("success", message);
}

function infoNotification(message) {
    createNotification("info", message);
}

function warningNotification(message) {
    createNotification("warning", message);
}

function errorNotification(message) {
    createNotification("error", message);
}

function createNotification(type, message) {
    let container = createContainer(type);
    createHeader(container, type);
    createContent(container, message);
    initToast(container);
}

function createContainer(type) {
    let container = $("<div></div>");
    container.addClass("toast");
    //container.attr("data-bs-autohide", false);
    if (type === "error") {
        container.attr("alert", "status");
        container.attr("aria-live", "assertive");
    } else {
        container.attr("role", "status");
        container.attr("aria-live", "polite");
    }
    return container;
}

function createHeader(container, type) {
    let header = $("<div></div>");
    header.addClass("toast-header").addClass(type);
    createHeaderTitle(header, type);
    header.append("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"toast\" aria-label=\"Close\"></button>");
    container.append(header);
}

function createHeaderTitle(header, type) {
    let title = $("<strong></strong>")
    title.addClass("me-auto bi").addClass(getIconClass(type));
    title.html("&nbsp;" + getHeaderTitle(type));
    header.append(title);
}

function getIconClass(type) {
    switch(type) {
        case "success":
            return "bi-check-circle-fill";
        case "info":
            return "bi-info-circle-fill";
        case "warning":
        case "error":
            return "bi-exclamation-triangle-fill";
    }
}

function getHeaderTitle(type) {
    switch(type) {
        case "success":
            return "Success";
        case "info":
            return "Information";
        case "warning":
            return "Warning";
        case "error":
            return "Error";
    }
}

function createContent(container, message) {
    let content = $("<div></div>");
    content.addClass("toast-body");
    content.html(message);
    container.append(content);
}

function initToast(container) {
    let notifications = $("#notifications");
    notifications.append(container);
    let toast = bootstrap.Toast.getOrCreateInstance(container);
    toast.show();
    container.on("hidden.bs.toast", function () {
        container.remove();
    });

    verifyMax(notifications);
}

const MAX_NOTIFICATIONS_NUMBER = 6;

function verifyMax(notifications) {
    // If number of notifications is greater than configured value
    // then hide them faster than auto hiding. This will remove them
    // completely.
    if (notifications.children().length > MAX_NOTIFICATIONS_NUMBER) {
        bootstrap.Toast.getInstance(notifications.children().first()).hide();
    }
}