$(document).ready(function () {
    const modalLoadUrl = "/ui/invoice-add/";
    $("#invoice-add").click(function() {
        const modals = $("#modals");

        $.ajax({
            "method": "get",
            "url": modalLoadUrl
        }).done(function (fragment) { // get from controller
            modals.prepend(fragment);

            const invoiceModal = $("#invoice-add-modal");
            if(invoiceModal.length) {
                let modal = bootstrap.Modal.getOrCreateInstance(invoiceModal);
                modal.show();

                $("#invoice-add-modal :input").inputmask(); // https://api.jquery.com/input-selector/

                invoiceModal.on("hidden.bs.modal", function () {
                    invoiceModal.remove();
                });

                const invoiceForm = $("#invoice-add-modal .modal-form");
                if(invoiceForm.length) {
                    invoiceForm.submit(function (event) {
                        event.preventDefault();

                        const form = $(this);
                        const url = form.attr('action');

                        $.ajax({
                            "method": "post",
                            "url": url,
                            "data": form.serialize()
                        }).done(function () {
                            invoiceForm.trigger("widget:reload", {
                                "widgetName": "invoice-list-widget"
                            });
                            invoiceForm.trigger("widget:reload", {
                                "widgetName": "repository-widget"
                            });
                            successNotification("Invoice added successfully!");
                        }).fail(function () {
                            errorNotification("Invoice adding failed!");
                        }).always(function () {
                            modal.hide();
                        });
                    });
                }
            }
        }).fail(function () {
            errorNotification("Can't load modal. Please try again.");
        });
    });
});