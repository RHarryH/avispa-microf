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

        const modal = document.querySelector(modalId);
        if(modal) {
            const formElements = modal.getElementsByClassName("modal-form")[0].elements;
            Array.from(formElements).forEach(function(element) {
                element.addEventListener("input", runCustomValidation);
                if(element.hasAttribute("data-inputmask") || element.hasAttribute("data-inputmask-regex")) {
                    Inputmask().mask(element);
                }
            });

            handleAddingTableRow(modalId, resourcePath, resourceId);
            handleTableDeletion(modalId); // this actually initializes it for the first time

            modal.addEventListener("hidden.bs.modal", function () { // remove modal when hidden
                modal.remove();
            });
            modal.addEventListener("show.bs.modal", function () { // remove modal when hidden
                conditionsCheck(this.querySelector(".modal-form")); // initialize modal
            });

            const bootstrapModal = bootstrap.Modal.getOrCreateInstance(modal);
            bootstrapModal.show();

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
            const url = form.getAttribute('action');
            if (url.length) {
                $.post({
                    "url": url,
                    "data": $(form).serialize()
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

                const modal = document.querySelector(modalId);

                const formElements = modal.getElementsByClassName("modal-form")[0].elements;
                Array.from(formElements).forEach(function(element) {
                    element.addEventListener("input", runCustomValidation);
                    if(element.hasAttribute("data-inputmask") || element.hasAttribute("data-inputmask-regex")) {
                        Inputmask().mask(element);
                    }
                });

                handleAddingTableRow(modalBody, resourcePath, resourceId);
                handleTableDeletion(modalBody); // this actually initializes it for the first time

                bootstrap.Tab.getInstance(document.querySelector("#modal-list a[href='#tab-pane-" + to + "'")).show();
            }).fail(function (e) {
                let errorMessage = composeErrorMessage("Loading next page failed", e);
                errorNotification(errorMessage);
            })
        }

        const forms = document.querySelectorAll(modalId + " .modal-form");
        forms.forEach(function(form) {
            form.addEventListener("submit", function (event) {
                event.preventDefault();

                const button = event.submitter;
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
            Array.from(form.elements).forEach(function (element) {
                element.addEventListener("input", function() {
                    conditionsCheck(form);
                });
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