// ===================================
// FUNCIONÁRIOS
// ===================================
let funcionarios = [];

document.addEventListener('DOMContentLoaded', async () => {
    await carregarFuncionarios();
});

async function carregarFuncionarios() {
    funcionarios = await api.get('/funcionarios/todos') || [];
    renderizarTabela(funcionarios);
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-funcionarios');

    if (lista.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" class="text-center"
                    style="padding: 32px; color: #999;">
                    Nenhum funcionário encontrado
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = lista.map(f => `
        <tr>
            <td><strong>${f.nome}</strong></td>
            <td>${f.cpf}</td>
            <td>${f.funcao}</td>
            <td>${f.telefone || '—'}</td>
            <td>${formatarData(f.dataAdmissao)}</td>
            <td>
                ${f.camisa ? `Camisa: ${f.camisa}` : ''}
                ${f.calca  ? ` | Calça: ${f.calca}` : ''}
                ${f.bota   ? ` | Bota: ${f.bota}`   : ''}
                ${!f.camisa && !f.calca && !f.bota ? '—' : ''}
            </td>
            <td>${badgeStatus(f.status)}</td>
            <td>
                <div class="flex gap-8">
                    <a class="btn btn-sm"
                        style="background:#e3f2fd;color:#1565c0;border:none;cursor:pointer;text-decoration:none;"
                        href="/siget-frontend/pages/ficha-funcionario.html?id=${f.id}">
                        📋 Ficha
                    </a>
                    <button class="btn btn-outline btn-sm"
                        onclick="abrirModalEdicao(${f.id})">
                        ✏️ Editar
                    </button>
                    ${f.ativo
                        ? `<button class="btn btn-sm"
                                style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                                onclick="desativarFuncionario(${f.id})">
                                🚫 Desativar
                           </button>`
                        : `<button class="btn btn-sm"
                                style="background:#e8f5e9;color:#2e7d32;border:none;cursor:pointer;"
                                onclick="reativarFuncionario(${f.id})">
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
    const filtrados = funcionarios.filter(f =>
        f.nome.toLowerCase().includes(busca) ||
        f.cpf.includes(busca) ||
        f.funcao.toLowerCase().includes(busca)
    );
    renderizarTabela(filtrados);
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
    document.getElementById('modal-titulo').textContent = 'Novo Funcionário';
    limparModal();
    document.getElementById('modal').classList.add('active');
}

function abrirModalEdicao(id) {
    const f = funcionarios.find(f => f.id === id);
    if (!f) return;

    document.getElementById('modal-titulo').textContent = 'Editar Funcionário';
    document.getElementById('funcionario-id').value  = f.id;
    document.getElementById('nome').value            = f.nome || '';
    document.getElementById('cpf').value             = f.cpf || '';
    document.getElementById('dataNascimento').value  = f.dataNascimento || '';
    document.getElementById('telefone').value        = f.telefone || '';
    document.getElementById('email').value           = f.email || '';
    document.getElementById('funcao').value          = f.funcao || '';
    document.getElementById('dataAdmissao').value    = f.dataAdmissao || '';
    document.getElementById('rg').value              = f.rg || '';
    document.getElementById('rgOrgaoEmissor').value  = f.rgOrgaoEmissor || '';
    document.getElementById('rgDataEmissao').value   = f.rgDataEmissao || '';
    document.getElementById('rgDataVencimento').value= f.rgDataVencimento || '';
    document.getElementById('pisNumero').value       = f.pisNumero || '';
    document.getElementById('ctpsNumero').value      = f.ctpsNumero || '';
    document.getElementById('ctpsSerie').value       = f.ctpsSerie || '';
    document.getElementById('camisa').value          = f.camisa || '';
    document.getElementById('calca').value           = f.calca || '';
    document.getElementById('bota').value            = f.bota || '';
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

function limparModal() {
    const campos = [
        'funcionario-id','nome','cpf','dataNascimento','telefone','email',
        'dataAdmissao','rg','rgOrgaoEmissor','rgDataEmissao','rgDataVencimento',
        'pisNumero','ctpsNumero','ctpsSerie','calca','bota'
    ];
    campos.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.value = '';
    });
    document.getElementById('funcao').value = '';
    document.getElementById('camisa').value = '';
}

// ===================================
// CRUD
// ===================================
async function salvarFuncionario() {
    const id = document.getElementById('funcionario-id').value;

    const data = {
        nome:             document.getElementById('nome').value,
        cpf:              document.getElementById('cpf').value,
        dataNascimento:   document.getElementById('dataNascimento').value || null,
        telefone:         document.getElementById('telefone').value,
        email:            document.getElementById('email').value,
        funcao:           document.getElementById('funcao').value,
        dataAdmissao:     document.getElementById('dataAdmissao').value || null,
        rg:               document.getElementById('rg').value,
        rgOrgaoEmissor:   document.getElementById('rgOrgaoEmissor').value,
        rgDataEmissao:    document.getElementById('rgDataEmissao').value || null,
        rgDataVencimento: document.getElementById('rgDataVencimento').value || null,
        pisNumero:        document.getElementById('pisNumero').value,
        ctpsNumero:       document.getElementById('ctpsNumero').value,
        ctpsSerie:        document.getElementById('ctpsSerie').value,
        camisa:           document.getElementById('camisa').value,
        calca:            document.getElementById('calca').value,
        bota:             document.getElementById('bota').value
    };

    if (!data.nome || !data.cpf || !data.funcao) {
        alert('Preencha os campos obrigatórios: Nome, CPF e Função.');
        return;
    }

    let resultado;
    if (id) {
        resultado = await api.put(`/funcionarios/${id}`, data);
    } else {
        resultado = await api.post('/funcionarios', data);
    }

    if (resultado) {
        fecharModal();
        await carregarFuncionarios();
    } else {
        alert('Erro ao salvar. Verifique os dados e tente novamente.');
    }
}

async function desativarFuncionario(id) {
    if (!confirm('Deseja desativar este funcionário?')) return;
    const ok = await api.delete(`/funcionarios/${id}`);
    if (ok) await carregarFuncionarios();
}

async function reativarFuncionario(id) {
    const f = funcionarios.find(f => f.id === id);
    if (!f) return;
    const data = { ...f, ativo: true, status: 'ATIVO' };
    const ok = await api.put(`/funcionarios/${id}`, data);
    if (ok) await carregarFuncionarios();
}
