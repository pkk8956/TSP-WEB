function sendDataFromChangePathPage() {
    const newPath = document.getElementById('path').value;
    $.ajax({
        type: "POST",
        url: "/processSaveButtonChangePathPaige",
        data: { newPath: newPath
        },
        success: function(response) {
        },
        error: function(error) {
        }
    });
}
