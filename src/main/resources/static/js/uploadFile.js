document.getElementById('fileUploadForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    fetch('/uploadFile', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                throw new Error('');
            }
        })
        .catch(error => console.error('Error:', error));
});
