/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

$(document).ready(function () {
    registerListWidget("invoice",
        ["repository-widget"],
        "Invoice deleted successfully!",
        "Error when deleting invoice!");
    registerListWidget("customer",
        [],
        "Customer deleted successfully!",
        "Error when deleting customer!");
    registerListWidget("bank-account",
        [],
        "Bank account deleted successfully!",
        "Error when deleting bank account!");

    registerRepositoryWidget();
    registerWidgetReloadEvent();

    // load widgets
    $(document).trigger("widget:load", {
        "widgetName": "list-widget",
        "resourceId": "invoice",
        "componentId": "nav-invoice"
    });
    $(document).trigger("widget:load", {
        "widgetName": "list-widget",
        "resourceId": "customer",
        "componentId": "nav-customer"
    });
    $(document).trigger("widget:load", {
        "widgetName": "list-widget",
        "resourceId": "bank-account",
        "componentId": "nav-bank-account"
    });
    $(document).trigger("widget:load", {
        "widgetName": "repository-widget",
        "componentId": "nav-repository"
    });
    $(document).trigger("widget:load", {
        "widgetName": "properties-widget",
        "componentId": "nav-properties"
    });
});

function registerListWidget(resourceId,
                            widgetsToReload,
                            deletionSuccessMessage,
                            deletionFailMessage) {
    $("#nav-" + resourceId).on("click", ".modal-accept-button", function (e) { // when modal form will be clicked
        e.preventDefault(); // disable default behavior

        const id = $("#" + resourceId + "-delete-modal .modal-accept-button").attr("value");

        // call deletion event
        $.ajax({
            "method": "delete",
            "url": "/" + resourceId + "/delete/" + id
        }).done(function () {
            // trigger reloads of widgets
            reloadWidgets(null, ["list-widget"].concat(widgetsToReload), resourceId);
            successNotification(deletionSuccessMessage);
        }).fail(function(e) {
            let errorMessage = composeErrorMessage(deletionFailMessage, e);
            errorNotification(errorMessage);
        });
    }).on("click", "." + resourceId + "-delete-button", function () { // modal is created once but object id varies in each row and has to be added dynamically
        const id = $(this).attr("value");
        $("#" + resourceId + "-delete-modal .modal-accept-button").attr("value", id);
    }).on("click", "#" + resourceId + "-list-refresh-button", function () {
        reloadWidgets(null,["list-widget"], resourceId);
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
    $(document).on("widget:load", function (event, data) {
        const component = $("#" + data.componentId);
        if(component.length) {
            // initialize tab trigger
            const tabTrigger = new bootstrap.Tab(document.querySelector("#" + data.componentId + "-tab"));

            let url = "widget/" + data.widgetName;
            if(data.hasOwnProperty("resourceId")) {
                url += "/" + data.resourceId;
            }

            $.ajax({
                "method": "get",
                "url": url
            }).done(function (fragment) { // get from controller
                component.prepend(fragment);

                initializeWidgets(data.widgetName, data.resourceId);
            })/*.fail(function (jqXHR) {
                //const html = $(jqXHR.responseText).attr("id", widgetName); // add widget identifier
                const html = getErrorPage(jqXHR.responseJSON.message, widgetName);
                component.prepend(html); // replace widget content with error page
            })*/;
        }
    });

    $(document).on("widget:reload", function (event, data) {
        const isListWidget = data.widgetName === "list-widget" && data.resourceId;
        let url = data.widgetName;
        if(isListWidget) {
            url += "/" + data.resourceId;
        }
        if(data.hasOwnProperty("id")) {
            url += "/" + data.id;
        }

        const widget = $("#" + (isListWidget ? data.resourceId + "-" : "") + data.widgetName);

        if(widget.length) {
            $.ajax({
                "method": "get",
                "url": "widget/" + url
            }).done(function (fragment) { // get from controller
                widget.replaceWith(fragment); // update snippet of page

                initializeWidgets(data.widgetName, data.resourceId);
            })/*.fail(function (jqXHR) {
                //const html = $(jqXHR.responseText).attr("id", data.widgetName);  // add widget identifier
                const html = getErrorPage(jqXHR.responseJSON.message, data.widgetName);
                widget.replaceWith(html); // replace widget content with error page
            })*/;
        }
    });

    $(document).on("widget:focus", function (event, data) {
        const widgetId = "#" + (data.resourceId ? data.resourceId + "-" : "") + data.widgetName;
        const componentId = document.querySelector(widgetId).parentElement.id;
        const tab = document.querySelector("#" + componentId + "-tab");
        bootstrap.Tab.getInstance(tab).show();
    });
}

function initializeWidgets(widgetName, resourceId) {
    switch (widgetName) {
        case "list-widget": {
            switch(resourceId) {
                case "invoice":
                    registerInvoiceUpdateModal();
                    break;
                case "customer":
                    registerCustomerUpdateModal();
                    break;
                case "bank-account":
                    registerBankAccountUpdateModal();
                    break;
            }

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

function reloadWidgets(source, widgetsNames, resourceId) {
    for(const widgetName of widgetsNames) {
        if(source == null) {
            $(document).trigger("widget:reload", {
                "widgetName": widgetName,
                "resourceId": resourceId
            });
        } else {
            $(source).trigger("widget:reload", {
                "widgetName": widgetName,
                "resourceId": resourceId
            });
        }
    }
}

function focusWidget(source, widgetName, resourceId) {
    if(source == null) {
        $(document).trigger("widget:focus", {
            "widgetName": widgetName,
            "resourceId": resourceId
        });
    } else {
        $(source).trigger("widget:focus", {
            "widgetName": widgetName,
            "resourceId": resourceId
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
