const server = 'http://localhost:8080';
const postUrl = server + '/send';

const axiosInstance = axios.create();

// https://github.com/axios/axios/issues/853
//axiosInstance.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

const f = function () {
    return {

        ct(event) {
            console.log('clicked', event);
            event.preventDefault();
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

            axios.post(postUrl, formData, config)
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