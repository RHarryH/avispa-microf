$(document).ready(function () {
    createInvoiceAddModal();
    createCustomerAddModal();
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

function createCustomerAddModal() {
    $(".customer-add-button").click(function () {
        createModal(
            "#customer-add-modal",
            "/customer/add/",
            [],
            "Customer added successfully!",
            "Customer adding failed!");
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