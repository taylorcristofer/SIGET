// ===================================
// ALOCAÇÕES
// ===================================
let alocacoes = [];
let funcionarios = [];
let contratos = [];

document.addEventListener('DOMContentLoaded', async () => {
    await carregarFuncionarios();
    await carregarContratos();
    await carregarAlocacoes();
});

async function carregarFuncionarios() {
    funcionarios = await api.get('/funcionarios') || [];
    const select = document.getElementById('funcionarioId');
    funcionarios.forEach(f => {
        const option = document.createElement('option');
        option.value = f.id;
        option.textContent = `${f.nome} — ${f.funcao}`;
        select.appendChild(option);
    });
}

async function carregarContratos() {
    contratos = await api.get('/contratos') || [];
    const select = document.getElementById('contratoId');
    contratos.forEach(c => {
        const option = document.createElement('option');
        option.value = c.id;
        option.textContent = `${c.numero} — ${c.orgaoNome}`;
        select.appendChild(option);
    });
}

async function carregarAlocacoes() {
    alocacoes = await api.get('/alocacoes/todas') || [];
    renderizarTabela(alocacoes);
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-alocacoes');

    if (lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="9" class="text-center"
                    style="padding: 32px; color: #999;">
                    Nenhuma alocação encontrada
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = lista.map(a => `
        <tr>
            <td><strong>${a.funcionarioNome}</strong></td>
            <td>${a.funcionarioFuncao}</td>
            <td>
                ${a.orgaoSigla ?
                    `<span class="badge badge-info">${a.orgaoSigla}</span> ` : ''}
                ${a.orgaoNome}
            </td>
            <td>${a.contratoNumero}</td>
            <td>${badgeTurno(a.turno)}</td>
            <td>${formatarData(a.dataInicio)}</td>
            <td>${formatarData(a.dataFim)}</td>
            <td>${badgeStatus(a.status)}</td>
            <td>
                <div class="flex gap-8">
                    ${a.status === 'ATIVA' ? `
                        <button class="btn btn-outline btn-sm"
                            onclick="abrirModalEdicao(${a.id})">
                            ✏️ Editar
                        </button>
                        <button class="btn btn-sm"
                            style="background:#ffebee;color:#c62828;
                                   border:none;cursor:pointer;"
                            onclick="encerrarAlocacao(${a.id})">
                            🔴 Encerrar
                        </button>` : `
                        <span style="color:#999; font-size:13px;">Encerrada</span>
                    `}
                </div>
            </td>
        </tr>
    `).join('');
}

function filtrarTabela() {
    const busca = document.getElementById('busca').value.toLowerCase();
    const filtrados = alocacoes.filter(a =>
        a.funcionarioNome.toLowerCase().includes(busca) ||
        a.orgaoNome.toLowerCase().includes(busca) ||
        a.contratoNumero.toLowerCase().includes(busca)
    );
    renderizarTabela(filtrados);
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
    document.getElementById('modal-titulo').textContent = 'Nova Alocação';
    document.getElementById('alocacao-id').value = '';
    document.getElementById('funcionarioId').value = '';
    document.getElementById('contratoId').value = '';
    document.getElementById('funcaoAlocada').value = '';
    document.getElementById('turno').value = '';
    document.getElementById('dataInicio').value = '';
    document.getElementById('dataFim').value = '';
    document.getElementById('observacoes').value = '';
    document.getElementById('modal').classList.add('active');
}

function abrirModalEdicao(id) {
    const a = alocacoes.find(a => a.id === id);
    if (!a) return;

    document.getElementById('modal-titulo').textContent = 'Editar Alocação';
    document.getElementById('alocacao-id').value = a.id;
    document.getElementById('funcionarioId').value = a.funcionarioId;
    document.getElementById('contratoId').value = a.contratoId;
    document.getElementById('funcaoAlocada').value = a.funcaoAlocada;
    document.getElementById('turno').value = a.turno || '';
    document.getElementById('dataInicio').value = a.dataInicio || '';
    document.getElementById('dataFim').value = a.dataFim || '';
    document.getElementById('observacoes').value = a.observacoes || '';
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

// ===================================
// CRUD
// ===================================
async function salvarAlocacao() {
    const id = document.getElementById('alocacao-id').value;

    const data = {
        funcionarioId: document.getElementById('funcionarioId').value,
        contratoId: document.getElementById('contratoId').value,
        funcaoAlocada: document.getElementById('funcaoAlocada').value,
        turno: document.getElementById('turno').value || null,
        dataInicio: document.getElementById('dataInicio').value,
        dataFim: document.getElementById('dataFim').value || null,
        observacoes: document.getElementById('observacoes').value
    };

    if (!data.funcionarioId || !data.contratoId ||
        !data.funcaoAlocada || !data.dataInicio) {
        alert('Preencha os campos obrigatórios: Funcionário, Contrato, Função e Data de Início.');
        return;
    }

    let resultado;
    if (id) {
        resultado = await api.put(`/alocacoes/${id}`, data);
    } else {
        resultado = await api.post('/alocacoes', data);
    }

    if (resultado) {
        fecharModal();
        await carregarAlocacoes();
    } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
}

async function encerrarAlocacao(id) {
    if (!confirm('Deseja encerrar esta alocação?')) return;
    const ok = await api.delete(`/alocacoes/${id}/encerrar`);
    if (ok) await carregarAlocacoes();
}