function insertDataToChangePaige() {
    fetch('/get-dataChangePaige')
        .then(response => response.json())
        .then(data => {
            document.getElementById('path').value = data.path;
        })
        .catch(error => console.error('Error:', error));
}
