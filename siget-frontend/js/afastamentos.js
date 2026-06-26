// ===================================
// AFASTAMENTOS
// ===================================
let afastamentos = [];
let funcionarios = [];

document.addEventListener('DOMContentLoaded', async () => {
    await carregarFuncionarios();
    await carregarAfastamentos();
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

async function carregarAfastamentos() {
    const funcionarioId = document.getElementById('filtro-funcionario').value;
    if (funcionarioId) {
        afastamentos = await api.get(`/afastamentos/funcionario/${funcionarioId}`) || [];
    } else {
        const resultados = await Promise.all(
            funcionarios.filter(f => f.ativo).map(f =>
                api.get(`/afastamentos/funcionario/${f.id}`)
            )
        );
        afastamentos = resultados.flat().filter(Boolean);
    }
    aplicarFiltros();
}

function aplicarFiltros() {
    const busca = document.getElementById('busca').value.toLowerCase();
    const tipo = document.getElementById('filtro-tipo').value;

    let filtrados = afastamentos;

    if (tipo) {
        filtrados = filtrados.filter(a => a.tipo === tipo);
    }

    if (busca) {
        filtrados = filtrados.filter(a =>
            a.nomeFuncionario.toLowerCase().includes(busca) ||
            (a.observacao || '').toLowerCase().includes(busca)
        );
    }

    renderizarTabela(filtrados);
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-afastamentos');

    if (lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center" style="padding: 32px; color: #999;">
                    Nenhum afastamento encontrado
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = lista.map(a => `
        <tr>
            <td><strong>${a.nomeFuncionario}</strong></td>
            <td>${badgeTipo(a.tipo)}</td>
            <td>${formatarData(a.dataInicio)}</td>
            <td>${formatarData(a.dataFim)}</td>
            <td>${a.dias} dia${a.dias > 1 ? 's' : ''}</td>
            <td>${a.observacao || '—'}</td>
            <td>
                <div class="flex gap-8">
                    <button class="btn btn-outline btn-sm"
                        onclick="abrirModalEdicao(${a.id})">
                        ✏️ Editar
                    </button>
                    <button class="btn btn-sm"
                        style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                        onclick="desativarAfastamento(${a.id})">
                        🚫 Remover
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function badgeTipo(tipo) {
    const map = {
        'ATESTADO':           '<span class="badge badge-warning">Atestado</span>',
        'SUSPENSAO':          '<span class="badge badge-danger">Suspensão</span>',
        'INSS':               '<span class="badge badge-info">INSS</span>',
        'FALTA_JUSTIFICADA':  '<span class="badge" style="background:#f3e5f5;color:#6a1b9a;">Falta Justif.</span>',
        'LICENCA_MATERNIDADE':'<span class="badge badge-success">Lic. Maternidade</span>',
    };
    return map[tipo] || `<span class="badge badge-info">${tipo}</span>`;
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
    document.getElementById('modal-titulo').textContent = 'Registrar Afastamento';
    document.getElementById('afastamento-id').value = '';
    document.getElementById('funcionarioId').value = '';
    document.getElementById('tipo').value = '';
    document.getElementById('dias').value = '';
    document.getElementById('dataInicio').value = '';
    document.getElementById('dataFim').value = '';
    document.getElementById('observacao').value = '';
    document.getElementById('modal').classList.add('active');
}

function abrirModalEdicao(id) {
    const a = afastamentos.find(a => a.id === id);
    if (!a) return;

    document.getElementById('modal-titulo').textContent = 'Editar Afastamento';
    document.getElementById('afastamento-id').value = a.id;
    document.getElementById('funcionarioId').value = a.funcionarioId;
    document.getElementById('tipo').value = a.tipo || '';
    document.getElementById('dias').value = a.dias || '';
    document.getElementById('dataInicio').value = a.dataInicio || '';
    document.getElementById('dataFim').value = a.dataFim || '';
    document.getElementById('observacao').value = a.observacao || '';
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

// ===================================
// CRUD
// ===================================
async function salvarAfastamento() {
    const id = document.getElementById('afastamento-id').value;

    const data = {
        funcionarioId: parseInt(document.getElementById('funcionarioId').value),
        tipo: document.getElementById('tipo').value,
        dias: parseInt(document.getElementById('dias').value),
        dataInicio: document.getElementById('dataInicio').value,
        dataFim: document.getElementById('dataFim').value,
        observacao: document.getElementById('observacao').value || null
    };

    if (!data.funcionarioId || !data.tipo || !data.dias || !data.dataInicio || !data.dataFim) {
        alert('Preencha todos os campos obrigatórios.');
        return;
    }

    if (data.dataFim < data.dataInicio) {
        alert('A data de fim não pode ser anterior à data de início.');
        return;
    }

    let resultado;
    if (id) {
        resultado = await api.put(`/afastamentos/${id}`, data);
    } else {
        resultado = await api.post('/afastamentos', data);
    }

    if (resultado) {
        fecharModal();
        await carregarAfastamentos();
    } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
}

async function desativarAfastamento(id) {
    if (!confirm('Deseja remover este afastamento?')) return;
    const ok = await api.delete(`/afastamentos/${id}`);
    if (ok) await carregarAfastamentos();
}
