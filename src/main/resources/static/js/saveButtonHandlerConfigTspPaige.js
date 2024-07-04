function sendDataFromConfigTspPage() {
    const numOfCities = document.getElementById('numOfCities').value;
    const numOfTraveler = document.getElementById('numOfTraveler').value;
    const maxLoad = document.getElementById('maxLoad').value;
    const isCheckedUseConstraints = document.getElementById('useConstraints').checked;
    $.ajax({
        type: "POST",
        url: "/processSaveButtonConfigTSP",
        data: { numOfCities: numOfCities,
                numOfTraveler: numOfTraveler,
                checkboxForConstraints: isCheckedUseConstraints,
                maxLoad: maxLoad
                },
        success: function(response) {
        },
        error: function(error) {
            console.error(error);
        }
    });

}