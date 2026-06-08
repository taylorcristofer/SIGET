// ===================================
// ÓRGÃOS
// ===================================
let orgaos = [];

document.addEventListener('DOMContentLoaded', async () => {
    await carregarOrgaos();
});

async function carregarOrgaos() {
    orgaos = await api.get('/orgaos/todos') || [];
    renderizarTabela(orgaos);
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-orgaos');

    if(lista.length === 0){
        tbody.innerHTML = `
        <tr>
            <td colspan="7" class="text-center" style="padding: 32px; color: #999;">
            Nenhum órgão encontrado
            </td>
        </tr>`;
    return;
    }

    tbody.innerHTML = lista.map(o => `
        <tr>
            <td><strong>${o.nome}</strong></td>
            <td>${o.sigla || '—'}</td>
            <td>${o.cnpj || '—'}</td>
            <td>${o.cidade || '—'}</td>
            <td>
                ${o.contatoNome || '—'}
                ${o.contatoTel ? `<br><small style="color:#666">${o.contatoTel}</small>` : ''}
            </td>
            <td>${o.ativo ?
                '<span class="badge badge-success">Ativo</span>' :
                '<span class="badge badge-danger">Inativo</span>'
            }</td>
            <td>
                <div class="flex gap-8">
                    <button class="btn btn-outline btn-sm"
                        onclick="abrirModalEdicao(${o.id})">
                        ✏️ Editar
                    </button>
                    ${o.ativo ?
                        `<button class="btn btn-sm"
                            style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                            onclick="desativarOrgao(${o.id})">
                            🚫 Desativar
                        </button>` :
                        `<button class="btn btn-sm"
                            style="background:#e8f5e9;color:#2e7d32;border:none;cursor:pointer;"
                            onclick="reativarOrgao(${o.id})">
                            ✅ Reativar
                        </button>`
                    }
                </div>
            </td>
        </tr>
    `).join('');
}

function filtrarTabela() {
    const busca = document.getElementById('busca').value.toLowerCase();
    const filtrados = orgaos.filter(o =>
        o.nome.toLowerCase().includes(busca) ||
        (o.sigla && o.sigla.toLowerCase().includes(busca)) ||
        (o.cidade && o.cidade.toLowerCase().includes(busca))
    );
    renderizarTabela(filtrados);
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
    document.getElementById('modal-titulo').textContent = 'Novo Órgão';
    document.getElementById('orgao-id').value = '';
    document.getElementById('nome').value = '';
    document.getElementById('sigla').value = '';
    document.getElementById('cnpj').value = '';
    document.getElementById('endereco').value = '';
    document.getElementById('cidade').value = '';
    document.getElementById('contatoNome').value = '';
    document.getElementById('contatoTel').value = '';
    document.getElementById('modal').classList.add('active');
}

function abrirModalEdicao(id) {
    const o = orgaos.find(o => o.id === id);
    if (!o) return;

    document.getElementById('modal-titulo').textContent = 'Editar Órgão';
    document.getElementById('orgao-id').value = o.id;
    document.getElementById('nome').value = o.nome || '';
    document.getElementById('sigla').value = o.sigla || '';
    document.getElementById('cnpj').value = o.cnpj || '';
    document.getElementById('endereco').value = o.endereco || '';
    document.getElementById('cidade').value = o.cidade || '';
    document.getElementById('contatoNome').value = o.contatoNome || '';
    document.getElementById('contatoTel').value = o.contatoTel || '';
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

// ===================================
// CRUD
// ===================================
async function salvarOrgao() {
    const id = document.getElementById('orgao-id').value;

    const data = {
        nome: document.getElementById('nome').value,
        sigla: document.getElementById('sigla').value,
        cnpj: document.getElementById('cnpj').value,
        endereco: document.getElementById('endereco').value,
        cidade: document.getElementById('cidade').value,
        contatoNome: document.getElementById('contatoNome').value,
        contatoTel: document.getElementById('contatoTel').value
    };

    if (!data.nome) {
        alert('O nome do órgão é obrigatório.');
        return;
    }

    let resultado;
    if (id) {
        resultado = await api.put(`/orgaos/${id}`, data);
    } else {
        resultado = await api.post('/orgaos', data);
    }

    if (resultado) {
        fecharModal();
        await carregarOrgaos();
    } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
}

async function desativarOrgao(id) {
    if (!confirm('Deseja desativar este órgão?')) return;
    const ok = await api.delete(`/orgaos/${id}`);
    if (ok) await carregarOrgaos();
}

async function reativarOrgao(id) {
    const o = orgaos.find(o => o.id === id);
    if (!o) return;
    const data = { ...o, ativo: true };
    const ok = await api.put(`/orgaos/${id}`, data);
    if (ok) await carregarOrgaos();
}