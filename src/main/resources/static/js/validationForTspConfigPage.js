$(document).ready(function() {
    $("#modalConfigTSP").draggable();
    $('.modal').css('background-color', 'transparent');
    $('form input').on('input', function() {
        const inputIds = ['numOfCities', 'numOfTraveler', 'maxLoad'];
        const onlyNumbersPattern = /^\d+$/;
        const saveTspConfiguration = document.getElementById('saveTspConfiguration');
        let show = true;

        const elements = [
            {
                id: 'FillAllFields',
                condition: (values) => values.some(value => value === ''),
            },
            {
                id: 'ZeroValueNotAllowed',
                condition: (values) => values.some(value => parseInt(value) === 0),
            },
            {
                id: 'NumericFormatOnly',
                condition: (values) => values.some(value => !onlyNumbersPattern.test(value)),
            },
            {
                id: 'ValueTooLarge',
                condition: (values) => values.some(value => parseInt(value) > 200 || value > 2147483647),
            },
        ];

        const inputValues = inputIds.map(id => document.getElementById(id).value);
        elements.forEach(element => {
            const el = document.getElementById(element.id);
            if (element.condition(inputValues)) {
                saveTspConfiguration.setAttribute('disabled', 'disabled');
                el.style.display = 'block';
                show = false;
            } else {
                el.style.display = 'none';
            }
        });
        if (show) {
            saveTspConfiguration.removeAttribute('disabled');
        }
    });
});