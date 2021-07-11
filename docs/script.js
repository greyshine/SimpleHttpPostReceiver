const f = function () {
    return {

        setMsg(msg, errOrOk) {
            msg = msg == null || typeof msg == 'undefined' ? '' : '' + msg;
            let color = '';
            if (typeof errOrOk == 'boolean') {
                console.log('eoo', errOrOk, typeof errOrOk);
                color = errOrOk ? 'red' : 'green';
            }
            document.getElementById('message').style.color = color;
            document.getElementById('message').innerText = msg;
        },

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

            axios.post(url, formData, config)
                .then(
                    response => {
                        console.log('OK', response);
                        f.setMsg('OK - request was successfully send.', false);
                    },
                    failure => {
                        console.log('fail', failure);
                        f.setMsg('FAIL - ' + failure, true);
                    }
                ).catch(exception => {
                    console.log('exception', exception);
                    f.setMsg('Exception - ' + exception, true);
                }
            ).finally(() => {
                //console.log('ajax done');
            });
        }

    };
}();