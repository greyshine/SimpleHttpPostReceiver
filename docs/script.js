const f = function () {
    return {

        sendForm(event) {

            console.log('sendForm()', event);

            event.preventDefault();

            var formData = new FormData();

            const form = document.getElementById('form');
            for (let i = 0, l = form.length; i < l; i++) {

                if (form[i].name == null || form[i].name === '') {
                    continue;
                }

                formData.append(form[i].name, form[i].value);
            }

            const config = {
                headers: {
                    'Content-Type': 'multipart/form-data',
                }
            };


            const url = document.getElementById('serverAddress').value + '/send';

            alert(url);

            axios.post(url, formData, config)
                .then(
                    response => {
                        console.log('OK', response);
                    },
                    failure => {
                        console.log('fail', failure);
                    }
                ).catch(exception => {
                console.log('exception', exception);
            })
                .finally(() => {
                    console.log('ajax done');
                });

            return false;
        }

    };
}();