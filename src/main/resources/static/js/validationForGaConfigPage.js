$(document).ready(function() {
    $("#modalConfigGA").draggable();
    $('.modal').css('background-color', 'transparent');
    $('form input').on('input', function() {
        const inputIds = ['mutationRate', 'populationSize', 'maxStagnation', 'tournamentSize', 'numOfGeneration', 'elitismCount'];
        const onlyFloatPattern = /^\d+(\.\d+)?$/;
        const onlyNumbersPattern = /^\d+$/;
        const configGA = document.getElementById('configGA');
        let show = true;
        const elements = [
            {
                id: 'G_FillAllFields',
                condition: (values) => values.some(value => value === ''),
            },
            {
                id: 'G_ZeroValueNotAllowed',
                condition: (values) => values.some((value, index) =>
                    (index === 0 && parseFloat(value) === 0) ||
                    (index === 1 && parseInt(value) === 0) ||
                    (index === 2 && parseInt(value) === 0) ||
                    (index === 3 && parseInt(value) === 0) ||
                    (index === 4 && parseInt(value) === 0) ||
                    (index === 5 && parseInt(value) === 0)
                ),
            },
            {
                id: 'G_NumericFormatOnly',
                condition: (values) => values.some(value => !onlyFloatPattern.test(value) && !onlyNumbersPattern.test(value)),
            },
            {
                id: 'G_ValueTooLarge',
                condition: (values) => values.some((value, index) =>
                    (index === 0 && (parseFloat(value) > 1)) ||
                    (index === 1 && (parseInt(value) > 100000)) ||
                    (index === 2 && (parseInt(value) > parseInt(values[4]))) ||
                    (index === 3 && (parseInt(value) > parseInt(values[1]))) ||
                    (index === 4 && (parseInt(value) > 100000)) ||
                    (index === 5 && (parseInt(value) > parseInt(values[1])))
                ),
            },
        ];
        const inputValues = inputIds.map(id => document.getElementById(id).value);
        elements.forEach(element => {
            const el = document.getElementById(element.id);
            if (element.condition(inputValues)) {
                configGA.setAttribute('disabled', 'disabled');
                el.style.display = 'block';
                show = false;
            } else {
                el.style.display = 'none';
            }
        });
        if (show) {
            configGA.removeAttribute('disabled');
        }
    });
});
