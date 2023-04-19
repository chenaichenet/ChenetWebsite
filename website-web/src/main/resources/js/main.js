axios.defaults.baseURL = 'https://localhost:9000';
function login() {
    let email = document.getElementById('email').value;
    let password = document.getElementById('password').value;
    if (email.trim().length !== 0 && password.trim().length !== 0) {
        axios({
            methods: 'post',
            url: '/user/login',
            data: {
                email: email,
                password: password
            }
        })
    } else {
        alert("参数为空")
    }
}