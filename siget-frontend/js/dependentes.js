// ===================================
// DEPENDENTES
// ===================================
let dependentes = [];
let funcionarios = [];

document.addEventListener('DOMContentLoaded', async () => {
    await carregarFuncionarios();
    await carregarDependentes();
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

async function carregarDependentes() {
    const funcionarioId = document.getElementById('filtro-funcionario').value;
    if (funcionarioId) {
        dependentes = await api.get(`/dependentes/funcionario/${funcionarioId}`) || [];
    } else {
        // Carrega de todos os funcionários ativos
        const resultados = await Promise.all(
            funcionarios.filter(f => f.ativo).map(f =>
                api.get(`/dependentes/funcionario/${f.id}`)
            )
        );
        dependentes = resultados.flat().filter(Boolean);
    }
    renderizarTabela(dependentes);
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-dependentes');

    if (lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center" style="padding: 32px; color: #999;">
                    Nenhum dependente encontrado
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = lista.map(d => `
        <tr>
            <td><strong>${d.nome}</strong></td>
            <td>${badgeParentesco(d.parentesco)}</td>
            <td>${d.nomeFuncionario}</td>
            <td>${formatarData(d.dataNascimento)}</td>
            <td>${d.cpf || '—'}</td>
            <td>
                <div class="flex gap-8">
                    <button class="btn btn-outline btn-sm"
                        onclick="abrirModalEdicao(${d.id})">
                        ✏️ Editar
                    </button>
                    <button class="btn btn-sm"
                        style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                        onclick="desativarDependente(${d.id})">
                        🚫 Remover
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function filtrarTabela() {
    const busca = document.getElementById('busca').value.toLowerCase();
    const filtrados = dependentes.filter(d =>
        d.nome.toLowerCase().includes(busca) ||
        d.nomeFuncionario.toLowerCase().includes(busca) ||
        (d.parentesco || '').toLowerCase().includes(busca)
    );
    renderizarTabela(filtrados);
}

function badgeParentesco(p) {
    const map = {
        'FILHO':    '<span class="badge badge-info">Filho(a)</span>',
        'CONJUGE':  '<span class="badge badge-success">Cônjuge</span>',
        'PAI':      '<span class="badge badge-warning">Pai</span>',
        'MAE':      '<span class="badge badge-warning">Mãe</span>',
        'IRMAO':    '<span class="badge" style="background:#f3e5f5;color:#6a1b9a;">Irmão/Irmã</span>',
        'OUTRO':    '<span class="badge badge-info">Outro</span>',
    };
    return map[p] || `<span class="badge badge-info">${p || '—'}</span>`;
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
    document.getElementById('modal-titulo').textContent = 'Novo Dependente';
    document.getElementById('dependente-id').value = '';
    document.getElementById('funcionarioId').value = '';
    document.getElementById('nome').value = '';
    document.getElementById('parentesco').value = '';
    document.getElementById('dataNascimento').value = '';
    document.getElementById('cpf').value = '';
    document.getElementById('modal').classList.add('active');
}

function abrirModalEdicao(id) {
    const d = dependentes.find(d => d.id === id);
    if (!d) return;

    document.getElementById('modal-titulo').textContent = 'Editar Dependente';
    document.getElementById('dependente-id').value = d.id;
    document.getElementById('funcionarioId').value = d.funcionarioId;
    document.getElementById('nome').value = d.nome || '';
    document.getElementById('parentesco').value = d.parentesco || '';
    document.getElementById('dataNascimento').value = d.dataNascimento || '';
    document.getElementById('cpf').value = d.cpf || '';
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

// ===================================
// CRUD
// ===================================
async function salvarDependente() {
    const id = document.getElementById('dependente-id').value;

    const data = {
        funcionarioId: parseInt(document.getElementById('funcionarioId').value),
        nome: document.getElementById('nome').value,
        parentesco: document.getElementById('parentesco').value,
        dataNascimento: document.getElementById('dataNascimento').value || null,
        cpf: document.getElementById('cpf').value || null
    };

    if (!data.funcionarioId || !data.nome || !data.parentesco) {
        alert('Preencha os campos obrigatórios: Funcionário, Nome e Parentesco.');
        return;
    }

    let resultado;
    if (id) {
        resultado = await api.put(`/dependentes/${id}`, data);
    } else {
        resultado = await api.post('/dependentes', data);
    }

    if (resultado) {
        fecharModal();
        await carregarDependentes();
    } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
}

async function desativarDependente(id) {
    if (!confirm('Deseja remover este dependente?')) return;
    const ok = await api.delete(`/dependentes/${id}`);
    if (ok) await carregarDependentes();
}
