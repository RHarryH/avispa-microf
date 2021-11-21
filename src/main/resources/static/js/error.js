function getErrorPage(message, source) {
    let page = $("<div></div>");
    page.attr("id", source);
    page.addClass("container jumbotron text-center");
    createErrorContent(page, message, source);

    return page;
}

function createErrorContent(page, message, source) {
    page.append("<h1>Something went wrong! </h1>");
    page.append("<h2>Our Engineers are on it</h2>");
    page.append("<h2>" + message + "</h2>");

    let button = $("<a></a>");
    button.addClass("btn btn-primary bi bi-arrow-repeat");
    button.html(" Reload widget");
    button.click(function () {
        $(this).trigger("widget:reload", {
            "widgetName": source
        });
    })
    page.append(button);


}