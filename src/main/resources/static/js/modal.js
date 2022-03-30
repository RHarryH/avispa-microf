$(document).ready(function () {
    // invoice
    registerAddModal(
        ["invoice"],
        ["invoice-list-widget", "repository-widget"],
        "invoice-list-widget",
        "Invoice added successfully!",
        "Invoice adding failed!");
    registerCloneModal(
        ["invoice"],
        ["invoice-list-widget", "repository-widget"],
        "invoice-list-widget",
        "Invoice cloned successfully!",
        "Invoice cloning failed!");

    // retail
    registerAddModal(
        ["customer", "retail"],
        ["customer-list-widget"],
        "customer-list-widget",
        "Retail customer added successfully!",
        "Retail customer adding failed!");

    // corporate
    registerAddModal(
        ["customer", "corporate"],
        ["customer-list-widget"],
        "customer-list-widget",
        "Corporate customer added successfully!",
        "Corporate customer adding failed!");
});

function registerInvoiceUpdateModal() {
    registerUpdateModal(
        ["invoice"],
        ["invoice-list-widget", "repository-widget"],
        null,
        "Invoice updated successfully!",
        "Invoice update failed!");
}

function registerRetailCustomerUpdateModal() {
    registerUpdateModal(
        ["customer", "retail"],
        ["customer-list-widget"],
        null,
        "Retail customer updated successfully!",
        "Retail customer update failed!");
}

function registerCorporateCustomerUpdateModal() {
    registerUpdateModal(
        ["customer", "corporate"],
        ["customer-list-widget"],
        null,
        "Corporate customer updated successfully!",
        "Corporate customer update failed!");
}

function registerAddModal(
    resourcePrefixes,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    const classPrefix = resourcePrefixes.join("-");
    $("." + classPrefix + "-add-button").click(function () {
        createModal(
            resourcePrefixes,
            "add-modal",
            "/modal/add/",
            widgetsToReload,
            widgetToFocus,
            successMessage,
            failMessage);
    });
}

function registerUpdateModal(
    resourcePrefixes,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    const classPrefix = resourcePrefixes.join("-");
    $("." + classPrefix + "-update-button").click(function () {
        createModal(
            resourcePrefixes,
            "update-modal",
            "/modal/update/" + $(this).attr("value"),
            widgetsToReload,
            widgetToFocus,
            successMessage,
            failMessage);
    });
}

function registerCloneModal(
    resourcePrefixes,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    const classPrefix = resourcePrefixes.join("-");
    $("." + classPrefix + "-clone-button").click(function () {
        createModal(
            resourcePrefixes,
            "clone-modal",
            "/modal/clone/",
            widgetsToReload,
            widgetToFocus,
            successMessage,
            failMessage);
    });
}

function createModal(
    resourcePrefixes,
    modalUniqueName,
    modalGetUrl,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    const modals = $("#modals");
    const modalId = "#" + resourcePrefixes.join("-") + "-" + modalUniqueName;
    const resourcePath = "/" + resourcePrefixes.join("/");

    $.ajax({
        "method": "get",
        "url": resourcePath + modalGetUrl
    }).done(function (fragment) { // get from controller
        modals.prepend(fragment);

        const modal = $(modalId);
        if (modal.length) {
            let bootstrapModal = bootstrap.Modal.getOrCreateInstance(modal);
            bootstrapModal.show();

            // remove modal when hidden
            modal.on("hidden.bs.modal", function () {
                modal.remove();
            });

            handleAddingTableRow(modalId, resourcePath);
            handleTableDeletion(modalId); // this actually initializes it for the first time

            $(modalId + " :input").on("input", runCustomValidation).inputmask();

            const triggerTabList = [].slice.call(document.querySelectorAll("#modal-list a"));
            triggerTabList.forEach(function (triggerEl) {
                new bootstrap.Tab(triggerEl);
            })

            // submit form
            formSubmit(bootstrapModal);
        }
    }).fail(function () {
        errorNotification("Can't load modal. Please try again.");
    });

    function formSubmit(bootstrapModal) {
        function finalAction(form) {
            const url = form.attr('action');
            if (url.length) {
                $.post({
                    "url": url,
                    "data": form.serialize()
                }).done(function () {
                    reloadWidgets(form, widgetsToReload);
                    if(widgetToFocus !== null) {
                        focusWidget(form, widgetToFocus);
                    }
                    successNotification(successMessage);
                }).fail(function (e) {
                    let errorMessage = composeErrorMessage(failMessage, e);
                    errorNotification(errorMessage);
                }).always(function () {
                    bootstrapModal.hide();
                });
            }
        }

        function loadPage(from, to) {
            $.get({
                "url": resourcePath + "/modal/page/" + to,
                "data": $(modalId + " #tab-pane-" + from + " .modal-form").serialize()
            }).done(function (fragment) {
                const modalBody = modalId + " #tab-pane-" + to + " .modal-body";
                $(modalBody).html(fragment);

                handleAddingTableRow(modalBody, resourcePath);
                handleTableDeletion(modalBody); // this actually initializes it for the first time

                $(modalBody + " :input").on("input", runCustomValidation).inputmask();

                bootstrap.Tab.getInstance(document.querySelector("#modal-list a[href='#tab-pane-" + to + "'")).show();
            }).fail(function (e) {
                let errorMessage = composeErrorMessage("Loading next page failed", e);
                errorNotification(errorMessage);
            })
        }

        const forms = $(modalId + " .modal-form");
        forms.each(function(index) {
            const form = $(this);
            form.submit(function (event) {
                event.preventDefault();

                const button = event.originalEvent.submitter;
                if(button.classList.contains("modal-next-button")) {
                    const currentPage = parseInt(button.value);
                    const nextPage = Math.min(currentPage + 1, forms.length);

                    if(currentPage !== nextPage) {
                        loadPage(currentPage, nextPage);
                    }
                } else if(button.classList.contains("modal-previous-button")) {
                    const currentPage = parseInt(button.value);
                    const previousPage = Math.max(currentPage - 1, 0);

                    if(currentPage !== previousPage) {
                        loadPage(currentPage, previousPage);
                    }
                } else if(button.classList.contains("modal-accept-button")) {
                    finalAction(form);
                }
            });
        });
    }
}

function runCustomValidation() {
    this.setCustomValidity("");

    if (this.validity.valid) {
        const customValidation = this.getAttribute("data-custom-validation-function");
        if (customValidation) {
            if (!executeFunctionByName(customValidation, window, this.value)) {
                setValidationMessage.call(this);
            }
        }
    }
}

function setValidationMessage() {
    const validationMessage = this.getAttribute('data-custom-validation-message');
    if (validationMessage) {
        this.setCustomValidity(validationMessage);
    } else {
        this.setCustomValidity("Custom validation failed");
    }
}