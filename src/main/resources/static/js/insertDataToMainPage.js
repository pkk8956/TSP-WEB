document.addEventListener('DOMContentLoaded', function() {
    fetch('/get-dataMainPage')
        .then(response => response.json())
        .then(data => {
            document.getElementById('select').value = data.checkedMethod;
            console.log(data.checkedMethod)
        })
        .catch(error => console.error('Error:', error));
});
