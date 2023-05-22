function NIPIsValid(nip) {
    const weights = [6, 5, 7, 2, 3, 4, 5, 6, 7];
    nip = nip.replace(/[\s-]/g, '');

    if (nip.length === 10 && parseInt(nip, 10) > 0) {
        let sum = 0;
        for(let i = 0; i < 9; i++){
            sum += nip[i] * weights[i];
        }
        return (sum % 11) === Number(nip[9]);
    }
    return false;
}

function isNRB(nrb) {
    const weights = [3, 9, 7, 1, 3, 9, 7, 1];

    if(nrb.startsWith("PL")) {
        nrb = nrb.substring(2);
    }

    if(nrb.length !== 26) {
        return false;
    }

    const sortCode = nrb.substring(2, 10);

    function validateChecksum(sortCode) {
        let total = 0;
        for(let i = 0; i < sortCode.length(); i++) { // without last digit
            total += sortCode.charCodeAt(i) * weights[i];
        }
        return total % 10 === 0;
    }

    return validateChecksum(sortCode);
}