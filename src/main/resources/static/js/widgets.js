$(document).ready(function () {

    registerInvoicesWidget();
    registerRepositoryWidget();
    registerPropertiesWidget();

    registerWidgetReloadEvent();

    // load widgets
    $(document).trigger("widget:load", ["invoice-list-widget", "#nav-invoices"]);
    $(document).trigger("widget:load", ["repository-widget", "#nav-repository"]);
    $(document).trigger("widget:load", ["properties-widget", "#nav-properties"]);
});

function registerInvoicesWidget() {
    $('#nav-invoices').on('click', '#modal-accept-form', function (e) { // when modal form will be clicked
        e.preventDefault(); // disable default behavior

        // call deletion event
        $.ajax({
            'method': 'delete',
            'url': e.currentTarget.action
        }).done(function () {
            // trigger reloads of widgets
            $(document).trigger("widget:reload", {
                "widgetName": "invoice-list-widget"
            });
            $(document).trigger("widget:reload", {
                "widgetName": "repository-widget"
            });
        })
    }).on('click', '.delete-button', function () { // modal is created once but invoice id varies in each row and has to be added dynamically
        const id = $(this).parent().find("#invoice-id").val();
        $("#invoice-delete-modal").find("#modal-accept-form").attr("action", "/invoice/delete/" + id);
    });
}

function registerRepositoryWidget() {
    $(document).ready(function (e) {
        $.jstree.defaults.core.themes.variant = "large";

        $('#nav-repository').on('click', '#refresh-button', function (e) {
            e.preventDefault();

            const directoryTree = $('#directory-tree');

            directoryTree.jstree(true).settings.core.data = {
                'url': '/directory',
                'data': function (node) {
                    return {'id': node.id};
                }
            };
            directoryTree.jstree(true).refresh();
        });
    });
}

function registerPropertiesWidget() {
    /*$('#nav-properties').on('click', '#yolo', function () {
        $.get("properties/44a169d7-cd0f-4215-9f61-a44075bf7001").done(function(fragment) { // get from controller
            $("#text").replaceWith(fragment); // update snippet of page
        });
    });*/
}

function registerWidgetReloadEvent() {
    $(document).on("widget:load", function (event, widgetName, componentId) {
        $.ajax({
            'method': 'get',
            'url': "widget/" + widgetName
        }).done(function (fragment) { // get from controller
            $(componentId).prepend(fragment)

            if(widgetName === 'repository-widget') {
                createDirectoryTree();
            }
        }).fail(function (jqXHR) {
            const html = $(jqXHR.responseText).attr("id", widgetName); // add widget identifier
            $(componentId).prepend(html); // replace widget content with error page
        });
    });

    $(document).on("widget:reload", function (event, data) {
        let url = data.widgetName;
        if(data.hasOwnProperty("id")) {
            url += "/" + data.id;
        }

        $.ajax({
            'method': 'get',
            'url': "widget/" + url
        }).done(function (fragment) { // get from controller
            $('#' + data.widgetName).replaceWith(fragment); // update snippet of page

            if(data.widgetName === 'repository-widget') {
                createDirectoryTree();
            }
        }).fail(function (jqXHR) {
            const html = $(jqXHR.responseText).attr("id", data.widgetName);  // add widget identifier
            $('#' + data.widgetName).replaceWith(html); // replace widget content with error page
        });
    });
}

function createDirectoryTree() {
    const directoryTree = $('#directory-tree');

    if (!directoryTree.length) {
        return;
    }

    directoryTree.on('ready.jstree refresh.jstree', function (e) {
        const directoryTree = $('#directory-tree');

        const nodesNumber = directoryTree.jstree(true).get_json('#').length;
        if (nodesNumber > 0) {
            directoryTree.removeClass("d-none");
            $('#directory-empty-text').addClass("d-none");
            $('#export-button').removeClass("d-none");
        } else {
            directoryTree.addClass("d-none");
            $('#directory-empty-text').removeClass("d-none");
            $('#export-button').addClass("d-none");
        }
    }).on('changed.jstree', function (e, data) {
        if(data.action === 'select_node') {
            $(document).trigger("widget:reload", {
                "widgetName": "properties-widget",
                "id": data.node.id
            });
        } else if(data.action === 'deselect_node') { // do not pass id
            $(document).trigger("widget:reload", {
                "widgetName": "properties-widget"
            });
        }
    }).jstree(
        {
            'core': {
                'multiple': false,
                'themes': {"stripes": true, "dots": false},
                'data': {
                    'url': '/directory',
                    'data': function (node) {
                        return {'id': node.id};
                    }
                },
            },
            /*"types" : {
                "#" : {
                    "max_children" : 1,
                    //"max_depth" : 4,
                    "valid_children" : ["root"]
                },
                "root" : {
                    "icon" : "bi bi-archive-fill",
                    "valid_children" : ["default"]
                },
                "folder" : {
                    "icon" : "bi bi-folder-fill",
                    "valid_children" : ["default", "file"]
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
                    "icon" : "bi bi-file-earmark-word",
                    "valid_children" : []
                }
            },*/
            'plugins': [
                /*"types", */'sort'
            ]
        }
    );
}