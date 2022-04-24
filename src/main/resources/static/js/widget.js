$(document).ready(function () {
    registerInvoicesWidget();
    registerCustomersWidget();
    registerBankAccountWidget();

    registerRepositoryWidget();
    registerWidgetReloadEvent();

    // load widgets
    $(document).trigger("widget:load", ["invoice-list-widget", "nav-invoices"]);
    $(document).trigger("widget:load", ["customer-list-widget", "nav-customers"]);
    $(document).trigger("widget:load", ["bank-account-list-widget", "nav-bank-accounts"]);
    $(document).trigger("widget:load", ["repository-widget", "nav-repository"]);
    $(document).trigger("widget:load", ["properties-widget", "nav-properties"]);
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
    }).on("click", "#invoice-list-refresh-button", function () {
        reloadWidgets(null,["invoice-list-widget"])
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
    }).on("click", "#customer-list-refresh-button", function () {
        reloadWidgets(null,["customer-list-widget"])
    });
}

function registerBankAccountWidget() {
    $('#nav-bank-accounts').on("click", ".modal-accept-button", function (e) { // when modal form will be clicked
        e.preventDefault(); // disable default behavior

        const id = $("#bank-account-delete-modal .modal-accept-button").attr("value");

        // call deletion event
        $.ajax({
            "method": "delete",
            "url": "/bank/account/delete/" + id
        }).done(function () {
            // trigger reloads of widgets
            reloadWidgets(null, ["bank-account-list-widget"])
            successNotification("Bank account deleted successfully!");
        }).fail(function(e) {
            let errorMessage = composeErrorMessage("Error when deleting bank account!", e);
            errorNotification(errorMessage);
        });
    }).on("click", ".bank-account-delete-button", function () { // modal is created once but bank account id varies in each row and has to be added dynamically
        const id = $(this).attr("value");
        $("#bank-account-delete-modal .modal-accept-button").attr("value", id);
    }).on("click", "#bank-account-list-refresh-button", function () {
        reloadWidgets(null,["bank-account-list-widget"])
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
        const component = $("#" + componentId);
        if(component.length) {
            // initialize tab trigger
            let tabTrigger = new bootstrap.Tab(document.querySelector("#" + componentId + "-tab"));

            $.ajax({
                "method": "get",
                "url": "widget/" + widgetName
            }).done(function (fragment) { // get from controller
                component.prepend(fragment);

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

    $(document).on("widget:focus", function (event, data) {
        const componentId = document.querySelector("#" + data.widgetName).parentElement.id;
        const tab = document.querySelector("#" + componentId + "-tab");
        bootstrap.Tab.getInstance(tab).show();
    });
}

function initializeWidgets(widgetName) {
    switch (widgetName) {
        case "invoice-list-widget": {
            registerInvoiceUpdateModal();

            createTooltips();

            break;
        }
        case "customer-list-widget": {
            registerCustomerUpdateModal()

            createTooltips();

            break;
        }
        case "bank-account-list-widget": {
            registerBankAccountUpdateModal();

            createTooltips();

            break;
        }
        case "repository-widget":
            createDirectoryTree();
            break;
    }
}

function createTooltips() {
    let tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    let tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });
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

function focusWidget(source, widgetName) {
    if(source == null) {
        $(document).trigger("widget:focus", {
            "widgetName": widgetName
        });
    } else {
        source.trigger("widget:focus", {
            "widgetName": widgetName
        });
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
           focusWidget(null, "properties-widget");
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
