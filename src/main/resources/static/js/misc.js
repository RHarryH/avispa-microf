Inputmask.extendAliases({
    'customDecimal': {
        alias: "numeric",
        min: 0,
        max: 9999999.99,
        groupSeparator: '\xa0', // non-breaking space
        radixPoint: ',',
        autoGroup: true,
        autoUnmask: true,
        digits: 2,
        digitsOptional: false,
        allowPlus: false,
        allowMinus: false
    }
});

function executeFunctionByName(functionName, context /*, args */) {
    let args = Array.prototype.slice.call(arguments, 2);
    let namespaces = functionName.split(".");
    let func = namespaces.pop();
    for(let i = 0; i < namespaces.length; i++) {
        context = context[namespaces[i]];
    }
    return context[func].apply(context, args);
}