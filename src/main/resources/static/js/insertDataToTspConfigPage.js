function insertDataToTspConfigPage() {
    fetch('/get-dataConfigTSP')
        .then(response => response.json())
        .then(data => {
            document.getElementById('numOfCities').value = data.numOfCities;
            document.getElementById('numOfTraveler').value = data.numOfTraveler;
            document.getElementById('maxLoad').value = data.maxLoad;
            document.getElementById('useConstraints').checked = data.useConstraints === 'true';
        })
        .catch(error => console.error('Error:', error));
}

