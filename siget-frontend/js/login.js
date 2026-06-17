if (localStorage.getItem('siget_token')){
    window.location.href = '../index.html';
}

document.getElementById('senha').addEventListener('keydown', e => {
    if (e.key === 'Enter') fazerLogin();
});

async function fazerLogin() {
    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value;
    const btn = document.getElementById('btnLogin');
    const erroMsg = document.getElementById('erroMsg');

    if (!email || !senha) {
        mostrarErro('Preencha e-mail e senha.');
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Entrando...';
    erroMsg.style.display = 'none';

    try {
        const res = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({email, senha})
        });

        const data = await res.json();

        if (res.ok){
            localStorage.setItem('siget_token', data.token);
            localStorage.setItem('siget_email', data.email);
            localStorage.setItem('siget_role', data.role);
            window.location.href = '../index.html';
        } else {
            mostrarErro(data.erro || 'Erro ao fazer login.');
        } 
    } catch(err) {
         mostrarErro('Servidor Indisponível. Verifique o backend');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Entrar';
    }
}

function mostrarErro(msg) {
    const el = document.getElementById('erroMsg');
    el.textContent = msg;
    el.style.display = 'block';
}