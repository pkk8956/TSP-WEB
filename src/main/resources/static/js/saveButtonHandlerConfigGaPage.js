function sendDataFromGaConfigPage() {
    const mutationRate = document.getElementById('mutationRate').value;
    const populationSize = document.getElementById('populationSize').value;
    const maxStagnation = document.getElementById('maxStagnation').value;
    const tournamentSize = document.getElementById('tournamentSize').value;
    const numOfGeneration = document.getElementById('numOfGeneration').value;
    const elitismCount = document.getElementById('elitismCount').value;
    $.ajax({
        type: "POST",
        url: "/processSaveButtonConfigGA",
        data: { mutationRate: mutationRate,
            populationSize: populationSize,
            maxStagnation: maxStagnation,
            tournamentSize: tournamentSize,
            elitismCount: elitismCount,
            numOfGeneration: numOfGeneration
        },
        success: function(response) {
            console.log(response);
            console.log(this.data);
        },
        error: function(error) {
            console.error(error);
        }
    });
}