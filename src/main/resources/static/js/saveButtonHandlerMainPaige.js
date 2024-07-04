function sendDataFromMainPaige() {
    const selectedMethod = document.getElementById('select').value;
    $.ajax({
        type: "POST",
        url: "/RUN",
        data: {  selectedMethod: selectedMethod },
        success: function(response) {
            displayAlert(response);
            location.reload();
        },
        error: function(error) {
        }
    });
}
function displayAlert(message) {
    if (message && message.trim() !== '') {
        localStorage.setItem('errorText', message);
    }
}
window.onload = function () {
    const savedErrorText = localStorage.getItem('errorText');
    if (savedErrorText) {
        const errorText = document.getElementById('errorText');
        errorText.style.display = 'block';
        errorText.textContent = savedErrorText;
        localStorage.clear();
    }
};