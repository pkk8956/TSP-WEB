const fileInput = document.getElementById('fileInput');
const submitButton = document.getElementById('submitButton');
fileInput.addEventListener('change', function () {
    if (fileInput.value) {
        submitButton.removeAttribute('disabled');
    } else {
        submitButton.setAttribute('disabled', 'disabled');
    }
});

document.getElementById('select').addEventListener('change', function() {
    const selectedMethod = this.value;
    const openModalConfigGA = document.getElementById('openModalConfigGA');
    if (selectedMethod === 'B&B'){
        openModalConfigGA.setAttribute('disabled', 'disabled');
    } else {
        openModalConfigGA.removeAttribute('disabled');
    }
});


