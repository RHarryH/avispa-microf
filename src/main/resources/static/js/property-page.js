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

function conditionsCheck(form) {

    form.querySelectorAll('[data-visibility-conditions]')
        .forEach(control => {
            const visibilityConditions = control.getAttribute("data-visibility-conditions");
            const result = resolveConditions(form, visibilityConditions);
            setVisibility(control, result);
        });

    form.querySelectorAll('[data-requirement-conditions]')
        .forEach(control => {
            const requirementConditions = control.getAttribute("data-requirement-conditions");
            const result = resolveConditions(form, requirementConditions);
            setRequirement(control, result);
        });
}

function resolveConditions(form, conditions) {
    const formData = new FormData(form);
    let conditionsObject = JSON.parse(conditions);

    let result = true;

    for (let [key, value] of Object.entries(conditionsObject)) {
        result &&= resolveCondition(key, value);
        if(result === false) {
            console.log("And short-circuiting");
            return result;
        }
    }
    return result;

    function resolveCondition(key, value) {
        console.log(`${key}: ${value}`);

        function getValueToCompare(fullKey) {
            const element = form.querySelector("select[name='" + fullKey + "'],input[name='" + fullKey + "']:checked");
            if (element) { // multi-select not supported!
                if(element.type === 'select-one') {
                    return element.options[element.selectedIndex].innerText;
                } else if(element.type === 'radio') {
                    return element.labels[0].innerText;
                }
            } else {
                return formData.get(fullKey);
            }
        }

        if(key === "$and") {
            let result = true;
            value.forEach(element => {
                let [elementKey, elementValue] = Object.entries(element)[0];
                result &&= resolveCondition(elementKey, elementValue);
                if(result === false) {
                    console.log("And short-circuiting");
                    return result;
                }
                console.log("Anding result");
            });
            return result;
        } else if(key === "$or") {
            let result = false;
            value.forEach(element => {
                let [elementKey, elementValue] = Object.entries(element)[0];
                result ||= resolveCondition(elementKey, elementValue);
                if(result === true) {
                    console.log("Or short-circuiting");
                    return result;
                }
                console.log("Oring result");
            });
            return result;
        } else {
            const fullKey = "object." + key;
            const valueToCompare = getValueToCompare(fullKey);

            if(typeof value === 'object' && !Array.isArray(value) && value !== null) { // value is an object
                let [operator, nestedValue] = Object.entries(value)[0];
                console.log("'" + fullKey + "' " + operator + "? " + nestedValue);
                return comparators[operator](valueToCompare, nestedValue);
            } else {
                console.log("'" + fullKey + "' $eq? " + value);
                return comparators["$eq"](valueToCompare, value);
            }
        }
    }
}

const comparators = {
    "$eq": (a, b) => a === b,
    "$ne": (a, b) => a !== b,
    "$lt": (a, b) => a < b,
    "$lte": (a, b) => a <= b,
    "$gt": (a, b) => a > b,
    "$gte": (a, b) => a >= b
};

function setVisibility(element, conditionsResult) {
    function updateColumnLabelsWidth(increase) {
        element.querySelectorAll('label, legend').forEach(function (label) {
            let classIndex = -1;
            for (let i = 0; i < label.classList.length; i++) {
                if (label.classList[i].startsWith("col-sm-")) {
                    classIndex = i;
                    break;
                }
            }

            if (classIndex !== -1) {
                let sign = 2 * increase - 1;
                let classValue = label.classList[classIndex]; // get original value
                label.classList.remove(classValue); // remove it from class list
                let value = parseInt(classValue.slice(-1));
                value += 2 * sign;
                label.classList.add("col-sm-" + value); // add new class
            }
        });
    }

    if (conditionsResult) {
        if(element.classList.contains("d-none")) {
            element.classList.remove("d-none");
            updateColumnLabelsWidth(true);
        }
    } else {
        if(!element.classList.contains("d-none")) {
            element.classList.add("d-none");
            updateColumnLabelsWidth(false);

            // clear values
            $(element).find(":input").toArray().forEach(function (input) {
                if(input.type === "radio") {
                    input.checked = false;
                } else {
                    input.value = "";
                }
            });
        }
    }
}

function setRequirement(element, conditionsResult) {
    const legend = element.querySelector("legend");
    if(legend) {
        if(conditionsResult) {
            if (!legend.textContent.endsWith("*")) {
                legend.textContent = legend.textContent + '*';
            }
        } else {
            if (legend.textContent.endsWith("*")) {
                legend.textContent = legend.textContent.slice(0, -1);
            }
        }
    }

    $(element).find(":input").toArray().forEach(function (input) {
        if(conditionsResult) {
            input.setAttribute("required", "required");
            if (input.type !== "radio" && input.labels) {
                input.labels.forEach(function (label) {
                    if (!label.textContent.endsWith("*")) {
                        label.textContent = label.textContent + '*';
                    }
                })
            }
        } else {
            input.removeAttribute("required");
            if (input.type !== "radio" && input.labels) {
                input.labels.forEach(function(label) {
                    if(label.textContent.endsWith("*")) {
                        label.textContent = label.textContent.slice(0, -1);
                    }
                })
            }
        }
    });
}