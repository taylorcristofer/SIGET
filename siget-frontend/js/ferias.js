// ===================================
// FÉRIAS
// ===================================
let ferias = [];
let funcionarios = [];

document.addEventListener('DOMContentLoaded', async () => {
    await carregarFuncionarios();
    await carregarFerias();
});

async function carregarFuncionarios() {
    funcionarios = await api.get('/funcionarios/todos') || [];
    popularSelectFuncionarios();
}

function popularSelectFuncionarios() {
    const selects = ['filtro-funcionario', 'funcionarioId'];
    selects.forEach(id => {
        const el = document.getElementById(id);
        if (!el) return;
        const primeiraOpcao = el.options[0];
        el.innerHTML = '';
        el.appendChild(primeiraOpcao);
        funcionarios
            .filter(f => f.ativo)
            .forEach(f => {
                const opt = document.createElement('option');
                opt.value = f.id;
                opt.textContent = f.nome;
                el.appendChild(opt);
            });
    });
}

async function carregarFerias() {
    const funcionarioId = document.getElementById('filtro-funcionario').value;
    if (funcionarioId) {
        ferias = await api.get(`/ferias/funcionario/${funcionarioId}`) || [];
    } else {
        const resultados = await Promise.all(
            funcionarios.filter(f => f.ativo).map(f =>
                api.get(`/ferias/funcionario/${f.id}`)
            )
        );
        ferias = resultados.flat().filter(Boolean);
    }
    renderizarTabela(ferias);
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-ferias');

    if (lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center" style="padding: 32px; color: #999;">
                    Nenhum registro de férias encontrado
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = lista.map(f => `
        <tr>
            <td><strong>${f.nomeFuncionario}</strong></td>
            <td>${formatarData(f.dataInicio)}</td>
            <td>${formatarData(f.dataFim)}</td>
            <td>${f.diasDireito} dias</td>
            <td>${f.diasGozados} dias</td>
            <td>${badgeDiasRestantes(f.diasRestantes)}</td>
            <td>
                <div class="flex gap-8">
                    <button class="btn btn-outline btn-sm"
                        onclick="abrirModalEdicao(${f.id})">
                        ✏️ Editar
                    </button>
                    <button class="btn btn-sm"
                        style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                        onclick="desativarFerias(${f.id})">
                        🚫 Remover
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function badgeDiasRestantes(dias) {
    if (dias <= 0) return `<span class="badge badge-danger">${dias} dias</span>`;
    if (dias <= 10) return `<span class="badge badge-warning">${dias} dias</span>`;
    return `<span class="badge badge-success">${dias} dias</span>`;
}

function filtrarTabela() {
    const busca = document.getElementById('busca').value.toLowerCase();
    const filtrados = ferias.filter(f =>
        f.nomeFuncionario.toLowerCase().includes(busca)
    );
    renderizarTabela(filtrados);
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
    document.getElementById('modal-titulo').textContent = 'Registrar Férias';
    document.getElementById('ferias-id').value = '';
    document.getElementById('funcionarioId').value = '';
    document.getElementById('dataInicio').value = '';
    document.getElementById('dataFim').value = '';
    document.getElementById('diasDireito').value = '30';
    document.getElementById('diasGozados').value = '';
    document.getElementById('modal').classList.add('active');
}

function abrirModalEdicao(id) {
    const f = ferias.find(f => f.id === id);
    if (!f) return;

    document.getElementById('modal-titulo').textContent = 'Editar Férias';
    document.getElementById('ferias-id').value = f.id;
    document.getElementById('funcionarioId').value = f.funcionarioId;
    document.getElementById('dataInicio').value = f.dataInicio || '';
    document.getElementById('dataFim').value = f.dataFim || '';
    document.getElementById('diasDireito').value = f.diasDireito || 30;
    document.getElementById('diasGozados').value = f.diasGozados || '';
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

// ===================================
// CRUD
// ===================================
async function salvarFerias() {
    const id = document.getElementById('ferias-id').value;

    const data = {
        funcionarioId: parseInt(document.getElementById('funcionarioId').value),
        dataInicio: document.getElementById('dataInicio').value,
        dataFim: document.getElementById('dataFim').value,
        diasDireito: parseInt(document.getElementById('diasDireito').value),
        diasGozados: parseInt(document.getElementById('diasGozados').value)
    };

    if (!data.funcionarioId || !data.dataInicio || !data.dataFim || !data.diasGozados) {
        alert('Preencha todos os campos obrigatórios.');
        return;
    }

    if (data.dataFim < data.dataInicio) {
        alert('A data de fim não pode ser anterior à data de início.');
        return;
    }

    if (data.diasGozados > data.diasDireito) {
        alert('Os dias gozados não podem ser maiores que os dias de direito.');
        return;
    }

    let resultado;
    if (id) {
        resultado = await api.put(`/ferias/${id}`, data);
    } else {
        resultado = await api.post('/ferias', data);
    }

    if (resultado) {
        fecharModal();
        await carregarFerias();
    } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
}

async function desativarFerias(id) {
    if (!confirm('Deseja remover este registro de férias?')) return;
    const ok = await api.delete(`/ferias/${id}`);
    if (ok) await carregarFerias();
}
