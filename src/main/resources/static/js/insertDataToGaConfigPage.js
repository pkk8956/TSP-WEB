function insertDataToGaConfigPage() {
    fetch('/get-dataConfigGA')
        .then(response => response.json())
        .then(data => {
            document.getElementById('mutationRate').value = data.mutationRate;
            document.getElementById('populationSize').value = data.populationSize;
            document.getElementById('maxStagnation').value = data.maxStagnation;
            document.getElementById('tournamentSize').value = data.tournamentSize;
            document.getElementById('numOfGeneration').value = data.numOfGeneration;
            document.getElementById('elitismCount').value = data.elitismCount;
        })
        .catch(error => console.error('Error:', error));
}