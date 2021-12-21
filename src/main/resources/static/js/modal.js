$(document).ready(function () {
    createInvoiceAddModal();
    createRetailCustomerAddModal();
    createCorporateCustomerAddModal();
});

function createInvoiceAddModal() {
    $(".invoice-add-button").click(function () {
        createModal(
            "#invoice-add-modal",
            "/invoice/add/",
            ["invoice-list-widget", "repository-widget"],
            "Invoice added successfully!",
            "Invoice adding failed!");
    });
}

function createInvoiceUpdateModal() {
    $(".invoice-update-button").click(function () {
        createModal(
            "#invoice-update-modal",
            "/invoice/update/" + $(this).attr("value"),
            ["invoice-list-widget", "repository-widget"],
            "Invoice updated successfully!",
            "Invoice update failed!");
    });
}

function createRetailCustomerAddModal() {
    $(".retail-customer-add-button").click(function () {
        createModal(
            "#retail-customer-add-modal",
            "/customer/retail/add/",
            ["customer-list-widget"],
            "Retail customer added successfully!",
            "Retail customer adding failed!");
    });
}

function createRetailCustomerUpdateModal() {
    $(".retail-customer-update-button").click(function () {
        createModal(
            "#retail-customer-update-modal",
            "/customer/retail/update/" + $(this).attr("value"),
            ["customer-list-widget"],
            "Retail customer updated successfully!",
            "Retail customer update failed!");
    });
}

function createCorporateCustomerAddModal() {
    $(".corporate-customer-add-button").click(function () {
        createModal(
            "#corporate-customer-add-modal",
            "/customer/corporate/add/",
            ["customer-list-widget"],
            "Corporate customer added successfully!",
            "Corporate customer adding failed!");
    });
}

function createCorporateCustomerUpdateModal() {
    $(".corporate-customer-update-button").click(function () {
        createModal(
            "#corporate-customer-update-modal",
            "/customer/corporate/update/" + $(this).attr("value"),
            ["customer-list-widget"],
            "Corporate customer updated successfully!",
            "Corporate customer update failed!");
    });
}

function createModal(
    modalId,
    modalGetUrl,
    widgetsToReload,
    successMessage,
    failMessage
) {
    const modals = $("#modals");

    $.ajax({
        "method": "get",
        "url": modalGetUrl
    }).done(function (fragment) { // get from controller
        modals.prepend(fragment);

        const modal = $(modalId);
        if (modal.length) {
            let bootstrapModal = bootstrap.Modal.getOrCreateInstance(modal);
            bootstrapModal.show();

            $(modalId + " :input").inputmask(); // https://api.jquery.com/input-selector/

            // remove modal when hidden
            modal.on("hidden.bs.modal", function () {
                modal.remove();
            });

            const form = $(modalId + " .modal-form");
            if (form.length) {
                form.submit(function (event) {
                    event.preventDefault();

                    const url = form.attr('action');

                    $.ajax({
                        "method": "post",
                        "url": url,
                        "data": form.serialize()
                    }).done(function () {
                        reloadWidgets(form, widgetsToReload);
                        successNotification(successMessage);
                    }).fail(function () {
                        errorNotification(failMessage);
                    }).always(function () {
                        bootstrapModal.hide();
                    });
                });
            }
        }
    }).fail(function () {
        errorNotification("Can't load modal. Please try again.");
    });
}