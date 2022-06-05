function handleAddingTableRow(root, resourcePath, resourceIdentifier) {
    $(root + " button[id$=-table-add-button]").click(function (e) {
        e.preventDefault();

        const id = $(this).attr("id");
        const prefix = $(this).val();
        const tablePropertyName = id.substring(0, id.indexOf("-table-add-button"));
        const tableId = "#" + tablePropertyName + "-table";

        const rowTemplate = document.querySelector("#" + tablePropertyName + "-row-template");

        if(!rowTemplate.innerHTML.length) {
            $.ajax({
                "method": "post",
                "url": resourcePath + "/modal/row/" + resourceIdentifier + "/" + tablePropertyName
            }).done(function (fragment) {
                rowTemplate.innerHTML = fragment;
                addRow();
            }).fail(function () {
                errorNotification("Can't add new row for the table!");
            });
        } else {
            addRow();
        }

        function addRow() {
            const row = document.importNode(rowTemplate.content, true);

            const existingRowsNum = document.querySelector(root + " " + tableId + " tbody").childElementCount;

            // set row count
            row.querySelector(".row-count").textContent = "" + (existingRowsNum + 1);

            // append prefixes
            row.querySelectorAll("td [id][name]").forEach(function (element) {
                element.setAttribute("id", prefix + tablePropertyName + existingRowsNum + "." + element.getAttribute("id"));
                element.setAttribute("name", prefix + tablePropertyName + "[" + existingRowsNum + "]." + element.getAttribute("name"));
            });

            Inputmask().mask(row.querySelectorAll("input"));

            // handle delete button
            let deleteButton = row.querySelector("button");
            deleteButton.setAttribute("value", "" + existingRowsNum);
            handleTableRowDelete(deleteButton);

            // actual node insertion
            document.querySelector(root + " " + tableId + " tbody").appendChild(row);
        }
    });
}

function handleTableDeletion(root) {
    document.querySelectorAll(root + " tr button").forEach(function (e) {
        handleTableRowDelete(e); // add handling for delete button
    });
}

function handleTableRowDelete(deleteButton) {
    deleteButton.addEventListener("click", function (e) {
        e.preventDefault();

        const row = $(this).closest("tr");
        const rowIndex = parseInt($(this).parents("tr").children("th:first-of-type").text()) - 1;
        const tableBody = row.parent();

        // update indexes
        const nameRegex = /\[\d+\]\./;
        const idRegex = /\d+\./;
        $(tableBody.children().slice(rowIndex)).each(function(i, row) {
            // update row count number
            row.querySelector("th:first-of-type").textContent = (i + 1);

            // update inputs id and name
            row.querySelectorAll("td [id][name]").forEach(function (element) {
                element.setAttribute("id", element.getAttribute("id").replace(idRegex, rowIndex + "."));
                element.setAttribute("name", element.getAttribute("name").replace(nameRegex, "[" + rowIndex + "]."));
            });

            // update button
            let deleteButton = row.querySelector("button");
            deleteButton.setAttribute("value", "" + rowIndex);
        });

        row.remove(); // remove row
    });
}