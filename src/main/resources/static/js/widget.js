$(document).ready(function () {
    registerInvoicesWidget();
    registerCustomersWidget();
    registerRepositoryWidget();

    registerWidgetReloadEvent();

    // load widgets
    $(document).trigger("widget:load", ["invoice-list-widget", "#nav-invoices"]);
    $(document).trigger("widget:load", ["customer-list-widget", "#nav-customers"]);
    $(document).trigger("widget:load", ["repository-widget", "#nav-repository"]);
    $(document).trigger("widget:load", ["properties-widget", "#nav-properties"]);
});

function registerInvoicesWidget() {
    $('#nav-invoices').on("click", ".modal-accept-button", function (e) { // when modal form will be clicked
        e.preventDefault(); // disable default behavior

        const id = $("#invoice-delete-modal .modal-accept-button").attr("value");

        // call deletion event
        $.ajax({
            "method": "delete",
            "url": "/invoice/delete/" + id
        }).done(function () {
            // trigger reloads of widgets
            reloadWidgets(null, ["invoice-list-widget", "repository-widget"])
            successNotification("Invoice deleted successfully!");
        }).fail(function(e) {
            let errorMessage = composeErrorMessage("Error when deleting invoice!", e);
            errorNotification(errorMessage);
        });
    }).on("click", ".invoice-delete-button", function () { // modal is created once but invoice id varies in each row and has to be added dynamically
        const id = $(this).attr("value");
        $("#invoice-delete-modal .modal-accept-button").attr("value", id);
    });
}

function registerCustomersWidget() {
    $('#nav-customers').on("click", ".modal-accept-button", function (e) { // when modal form will be clicked
        e.preventDefault(); // disable default behavior

        const id = $("#customer-delete-modal .modal-accept-button").attr("value");

        // call deletion event
        $.ajax({
            "method": "delete",
            "url": "/customer/delete/" + id
        }).done(function () {
            // trigger reloads of widgets
            reloadWidgets(null, ["customer-list-widget"])
            successNotification("Customer deleted successfully!");
        }).fail(function(e) {
            let errorMessage = composeErrorMessage("Error when deleting customer!", e);
            errorNotification(errorMessage);
        });
    }).on("click", ".customer-delete-button", function () { // modal is created once but customer id varies in each row and has to be added dynamically
        const id = $(this).attr("value");
        $("#customer-delete-modal .modal-accept-button").attr("value", id);
    });
}

function registerRepositoryWidget() {
    $.jstree.defaults.core.themes.variant = "large";

    $("#nav-repository").on("click", "#refresh-button", function (e) {
        e.preventDefault();

        const directoryTree = $("#directory-tree");

        directoryTree.jstree(true).settings.core.data = {
            "url": "/directory",
            "data": function (node) {
                return {"id": node.id};
            }
        };
        directoryTree.jstree(true).refresh();
    });
}

function registerWidgetReloadEvent() {
    $(document).on("widget:load", function (event, widgetName, componentId) {
        const component = $(componentId);
        if(component.length) {
            $.ajax({
                "method": "get",
                "url": "widget/" + widgetName
            }).done(function (fragment) { // get from controller
                component.prepend(fragment)

                initializeWidgets(widgetName);
            })/*.fail(function (jqXHR) {
                //const html = $(jqXHR.responseText).attr("id", widgetName); // add widget identifier
                const html = getErrorPage(jqXHR.responseJSON.message, widgetName);
                component.prepend(html); // replace widget content with error page
            })*/;
        }
    });

    $(document).on("widget:reload", function (event, data) {
        let url = data.widgetName;
        if(data.hasOwnProperty("id")) {
            url += "/" + data.id;
        }

        const widget = $("#" + data.widgetName);

        if(widget.length) {
            $.ajax({
                "method": "get",
                "url": "widget/" + url
            }).done(function (fragment) { // get from controller
                widget.replaceWith(fragment); // update snippet of page

                initializeWidgets(data.widgetName);
            })/*.fail(function (jqXHR) {
                //const html = $(jqXHR.responseText).attr("id", data.widgetName);  // add widget identifier
                const html = getErrorPage(jqXHR.responseJSON.message, data.widgetName);
                widget.replaceWith(html); // replace widget content with error page
            })*/;
        }
    });
}

function initializeWidgets(widgetName) {
    switch (widgetName) {
        case "invoice-list-widget": {
            createInvoiceUpdateModal();

            let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
            let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl)
            })

            break;
        }
        case "customer-list-widget": {
            createRetailCustomerUpdateModal()
            createCorporateCustomerUpdateModal();

            let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
            let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl)
            })
            break;
        }
        case "repository-widget":
            createDirectoryTree();
            break;
    }
}

function createDirectoryTree() {
    const directoryTree = $("#directory-tree");

    if (!directoryTree.length) {
        return;
    }

    directoryTree.on("ready.jstree refresh.jstree", function (e) {
        const directoryTree = $("#directory-tree");

        const nodesNumber = directoryTree.jstree(true).get_json("#").length;
        if (nodesNumber > 0) {
            directoryTree.removeClass("d-none");
            $("#directory-empty-text").addClass("d-none");
            $("#export-button").removeClass("d-none");
        } else {
            directoryTree.addClass("d-none");
            $("#directory-empty-text").removeClass("d-none");
            $("#export-button").addClass("d-none");
        }
    }).on("changed.jstree", function (e, data) {
        if(data.action === "select_node") {
            $(document).trigger("widget:reload", {
                "widgetName": "properties-widget",
                "id": data.node.id
            });
        } else if(data.action === "deselect_node") { // do not pass id
            $(document).trigger("widget:reload", {
                "widgetName": "properties-widget"
            });
        }
    }).jstree(
        {
            "core": {
                "multiple": false,
                "themes": {"stripes": true, "dots": false},
                "data": {
                    "url": "/directory",
                    "data": function (node) {
                        return {"id": node.id};
                    }
                },
            },
            "types" : {
                "#" : {
                    "max_children" : 1,
                    //"max_depth" : 4,
                    "valid_children" : ["root"]
                },
                "root" : {
                    "icon" : "bi bi-archive-fill"
                },
                "folder" : {
                    "icon" : "bi bi-folder-fill"
                },
                "default" : { // unknown file type
                    "icon" : "bi bi-file-earmark",
                    "valid_children" : []
                },
                "pdf" : {
                    "icon" : "bi bi-file-earmark-pdf",
                    "valid_children" : []
                },
                "odt" : {
                    "icon" : "bi bi-file-richtext",
                    "valid_children" : []
                },
                "rtf" : {
                    "icon" : "bi bi-file-richtext",
                    "valid_children" : []
                },
                "doc" : {
                    "icon" : "bi bi-file-earmark-word",
                    "valid_children" : []
                },
                "docx" : {
                    "icon" : "bi bi-file-earmark-word",
                    "valid_children" : []
                },
                "csv" : {
                    "icon" : "bi bi-file-earmark-text",
                    "valid_children" : []
                },
                "txt" : {
                    "icon" : "bi bi-file-earmark-zip",
                    "valid_children" : []
                },
                "zip" : {
                    "icon" : "bi bi-file-earmark-zip",
                    "valid_children" : []
                },
                "rar" : {
                    "icon" : "bi bi-file-earmark-zip",
                    "valid_children" : []
                }
            },
            "plugins": [
                "types", "sort"
            ]
        }
    );
}

function reloadWidgets(source, widgetsNames) {
    for(const widgetName of widgetsNames) {
        if(source == null) {
            $(document).trigger("widget:reload", {
                "widgetName": widgetName
            });
        } else {
            source.trigger("widget:reload", {
                "widgetName": widgetName
            });
        }
    }
}
