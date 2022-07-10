$(document).ready(function () {
    // invoice
    registerAddModal("invoice", ["list-widget", "repository-widget"], "list-widget", "Invoice added successfully!", "Invoice adding failed!");
    registerCloneModal(
        ["invoice"],
        ["list-widget", "repository-widget"],
        "list-widget",
        "Invoice cloned successfully!",
        "Invoice cloning failed!");

    // customer
    registerAddModal("customer", ["list-widget"], "list-widget", "Customer added successfully!", "Customer adding failed!");

    // bank account
    registerAddModal("bank-account", ["list-widget"], "list-widget", "Bank account added successfully!", "Bank account adding failed!");
});

function registerInvoiceUpdateModal() {
    registerUpdateModal(
        "invoice",
        ["list-widget", "repository-widget"],
        null,
        "Invoice updated successfully!",
        "Invoice update failed!");
}

function registerCustomerUpdateModal() {
    registerUpdateModal(
        "customer",
        ["list-widget"],
        null,
        "Customer updated successfully!",
        "Customer update failed!");
}

function registerBankAccountUpdateModal() {
    registerUpdateModal(
        "bank-account",
        ["list-widget"],
        null,
        "Bank account updated successfully!",
        "Bank account update failed!");
}

function registerAddModal(
    resourceId,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    $("." + resourceId + "-add-button").click(function () {
        createModal(
            resourceId,
            "add-modal",
            "/modal/add/" + resourceId,
            widgetsToReload,
            widgetToFocus,
            successMessage,
            failMessage);
    });
}

function registerUpdateModal(
    resourceId,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    $("." + resourceId + "-update-button").click(function () {
        createModal(
            resourceId,
            "update-modal",
            "/modal/update/" + resourceId + "/" + $(this).attr("value"),
            widgetsToReload,
            widgetToFocus,
            successMessage,
            failMessage);
    });
}

function registerCloneModal(
    resourceId,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    $("." + resourceId + "-clone-button").click(function () {
        createModal(
            resourceId,
            "clone-modal",
            "/modal/clone/" + resourceId,
            widgetsToReload,
            widgetToFocus,
            successMessage,
            failMessage);
    });
}

function createModal(
    resourceId,
    modalUniqueName,
    modalGetUrl,
    widgetsToReload,
    widgetToFocus,
    successMessage,
    failMessage
) {
    const modals = $("#modals");
    const modalId = "#" + resourceId + "-" + modalUniqueName;
    const resourcePath = "";

    $.ajax({
        "method": "get",
        "url": resourcePath + modalGetUrl
    }).done(function (fragment) { // get from controller
        modals.prepend(fragment);

        const modal = $(modalId);
        if (modal.length) {
            modal.on("hidden.bs.modal", function () { // remove modal when hidden
                modal.remove();
            }).on("show.bs.modal", function() {
                conditionsCheck(this.querySelector(".modal-form")); // initialize modal
            });

            let bootstrapModal = bootstrap.Modal.getOrCreateInstance(modal);
            bootstrapModal.show();

            handleAddingTableRow(modalId, resourcePath, resourceId);
            handleTableDeletion(modalId); // this actually initializes it for the first time

            $(modalId + " :input").on("input", runCustomValidation).inputmask();

            const triggerTabList = [].slice.call(document.querySelectorAll("#modal-list a"));
            triggerTabList.forEach(function (triggerEl) {
                new bootstrap.Tab(triggerEl);
            });

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
                    reloadWidgets(form, widgetsToReload, resourceId);
                    if(widgetToFocus !== null) {
                        focusWidget(form, widgetToFocus, resourceId);
                    }
                    successNotification(successMessage);
                }).fail(function (e) {
                    const errorMessage = composeErrorMessage(failMessage, e);
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

                handleAddingTableRow(modalBody, resourcePath, resourceId);
                handleTableDeletion(modalBody); // this actually initializes it for the first time

                $(modalBody + " :input").on("input", runCustomValidation).inputmask();

                bootstrap.Tab.getInstance(document.querySelector("#modal-list a[href='#tab-pane-" + to + "'")).show();
            }).fail(function (e) {
                let errorMessage = composeErrorMessage("Loading next page failed", e);
                errorNotification(errorMessage);
            })
        }

        const forms = $(modalId + " .modal-form");
        forms.each(function() {
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
            }).change(function () {
                conditionsCheck($(this)[0]);
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