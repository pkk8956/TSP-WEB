document.getElementById('openModalChangePath').addEventListener('click', function() {
    document.getElementById('modalChangePath').style.display = 'block';
});
document.getElementById('closeModalChangePath').addEventListener('click', function() {
    document.getElementById('modalChangePath').style.display = 'none';
    location.reload();
});