function executeFunctionByName(functionName, context /*, args */) {
    let args = Array.prototype.slice.call(arguments, 2);
    let namespaces = functionName.split(".");
    let func = namespaces.pop();
    for(let i = 0; i < namespaces.length; i++) {
        context = context[namespaces[i]];
    }
    return context[func].apply(context, args);
}