const pathInput = document.getElementById('path');
const notOnlyIndexes = document.getElementById('notOnlyIndexes');
const emptyPath = document.getElementById('emptyPath');
const notAllIndexes = document.getElementById('notAllIndexes');
const duplicates = document.getElementById('duplicates');
const changePathButton = document.getElementById('changePathButton');
const pattern1 = /^\[\d+(,\s*\d+)*]$/;

pathInput.addEventListener('input', function() {
    const value = this.value;
    const asArray = pattern1.test(value) ? JSON.parse(value) : undefined;
    let isActiveButton = true;
    emptyPath.style.display = value === '' ? 'block' : 'none';
    if (!asArray) {
        notOnlyIndexes.style.display = 'block';
        isActiveButton = false;
    } else {
        notOnlyIndexes.style.display = 'none';
        const firstIndex = asArray[0];
        const modified = asArray.filter((el, index) => index === 0 || el !== firstIndex);
        modified.sort((a, b) => a - b);
        notAllIndexes.style.display = modified.every((el, index) => el === 1 + index) ? 'none' : 'block';
        const asSet = new Set(modified);
        duplicates.style.display = asSet.size !== modified.length ? 'block' : 'none';
    }
    changePathButton.disabled = !isActiveButton;
    // adjustWidth(this);
});

// function adjustWidth(element) {
//     element.style.width = ((element.value.length + 1) * 8) + 'px';
// }
