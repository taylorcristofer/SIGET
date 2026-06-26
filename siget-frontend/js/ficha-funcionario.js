// ===================================
// FICHA DO FUNCIONÁRIO
// ===================================
let funcionario = null;
let categoriaAtual = '';

// Pega o ID da URL: ficha-funcionario.html?id=1
const params = new URLSearchParams(window.location.search);
const funcionarioId = params.get('id');

document.addEventListener('DOMContentLoaded', async () => {
    if (!funcionarioId) {
        alert('Funcionário não identificado.');
        window.location.href = '/siget-frontend/pages/funcionarios.html';
        return;
    }
    await carregarFicha();
    await carregarArquivos();
});

// ===================================
// CARREGAR DADOS
// ===================================
async function carregarFicha() {
    funcionario = await api.get(`/funcionarios/${funcionarioId}`);
    if (!funcionario) {
        alert('Funcionário não encontrado.');
        window.location.href = '/siget-frontend/pages/funcionarios.html';
        return;
    }
    preencherFicha(funcionario);
}

function preencherFicha(f) {
    // Topbar e avatar
    document.getElementById('topbar-nome').textContent = f.nome;
    document.getElementById('ficha-avatar').textContent = f.nome.charAt(0).toUpperCase();
    document.getElementById('ficha-nome').textContent = f.nome;
    document.getElementById('ficha-funcao').textContent = f.funcao || '—';
    document.getElementById('ficha-admissao').textContent = formatarData(f.dataAdmissao);
    document.getElementById('ficha-telefone').textContent = f.telefone || '—';
    document.getElementById('ficha-email').textContent = f.email || '—';
    document.getElementById('ficha-status-badge').innerHTML = badgeStatus(f.status);

    // Tab dados pessoais
    document.getElementById('d-nome').textContent       = f.nome || '—';
    document.getElementById('d-cpf').textContent        = f.cpf || '—';
    document.getElementById('d-nascimento').textContent = formatarData(f.dataNascimento);
    document.getElementById('d-telefone').textContent   = f.telefone || '—';
    document.getElementById('d-email').textContent      = f.email || '—';
    document.getElementById('d-funcao').textContent     = f.funcao || '—';
    document.getElementById('d-admissao').textContent   = formatarData(f.dataAdmissao);
    document.getElementById('d-status').innerHTML       = badgeStatus(f.status);
    document.getElementById('d-camisa').textContent     = f.camisa || '—';
    document.getElementById('d-calca').textContent      = f.calca || '—';
    document.getElementById('d-bota').textContent       = f.bota || '—';

    // Tab documentos
    document.getElementById('d-rg').textContent           = f.rg || '—';
    document.getElementById('d-rg-orgao').textContent     = f.rgOrgaoEmissor || '—';
    document.getElementById('d-rg-emissao').textContent   = formatarData(f.rgDataEmissao);
    document.getElementById('d-rg-vencimento').textContent = formatarData(f.rgDataVencimento);
    document.getElementById('d-cpf-doc').textContent      = f.cpf || '—';
    document.getElementById('d-pis').textContent          = f.pisNumero || '—';
    document.getElementById('d-ctps-numero').textContent  = f.ctpsNumero || '—';
    document.getElementById('d-ctps-serie').textContent   = f.ctpsSerie || '—';
}

// ===================================
// ARQUIVOS
// ===================================
async function carregarArquivos() {
    const arquivos = await api.get(`/arquivos/funcionario/${funcionarioId}`) || [];

    const categorias = [
        'DOCUMENTO_PESSOAL',
        'FOLHA_PONTO',
        'ATESTADO',
        'FERIAS',
        'CONTRACHEQUE'
    ];

    categorias.forEach(cat => {
        const lista = arquivos.filter(a => a.categoria === cat);
        renderizarArquivos(cat, lista);
    });
}

function renderizarArquivos(categoria, lista) {
    const container = document.getElementById(`lista-${categoria}`);
    if (!container) return;

    if (lista.length === 0) {
        const labels = {
            'DOCUMENTO_PESSOAL': 'documento pessoal',
            'FOLHA_PONTO':       'folha de ponto',
            'ATESTADO':          'atestado',
            'FERIAS':            'documento de férias',
            'CONTRACHEQUE':      'contracheque'
        };
        container.innerHTML = `
            <div class="empty-state">
                <span>📄</span>Nenhum ${labels[categoria] || 'arquivo'} salvo
            </div>`;
        return;
    }

    container.innerHTML = lista.map(a => `
        <div class="arquivo-item">
            <div class="arquivo-item-info">
                <span class="arquivo-icon">📄</span>
                <div>
                    <div class="arquivo-nome">${a.nomeArquivo}</div>
                    <div class="arquivo-meta">
                        ${a.mesReferencia ? `Ref: ${formatarMes(a.mesReferencia)} · ` : ''}
                        Adicionado em ${formatarDataHora(a.criadoEm)}
                    </div>
                </div>
            </div>
            <div class="arquivo-acoes">
                ${a.url
                    ? `<a href="${a.url}" target="_blank" class="btn btn-outline btn-sm">
                        👁️ Visualizar
                       </a>`
                    : `<button class="btn btn-outline btn-sm" disabled
                        title="Upload S3 pendente" style="opacity:0.5;">
                        ☁️ Pendente
                       </button>`
                }
                <button class="btn btn-sm"
                    style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                    onclick="removerArquivo(${a.id}, '${categoria}')">
                    🗑️
                </button>
            </div>
        </div>
    `).join('');
}

// ===================================
// TABS
// ===================================
function abrirTab(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));

    document.getElementById(`tab-${tab}`).classList.add('active');
    event.target.classList.add('active');
}

// ===================================
// MODAL UPLOAD
// ===================================
function abrirModalUpload(categoria) {
    categoriaAtual = categoria;

    const titulos = {
        'DOCUMENTO_PESSOAL': 'Adicionar Documento Pessoal',
        'FOLHA_PONTO':       'Adicionar Folha de Ponto',
        'ATESTADO':          'Adicionar Atestado',
        'FERIAS':            'Adicionar Documento de Férias',
        'CONTRACHEQUE':      'Adicionar Contracheque'
    };

    document.getElementById('modal-upload-titulo').textContent =
        titulos[categoria] || 'Adicionar Documento';
    document.getElementById('upload-nome').value = '';
    document.getElementById('upload-mes').value = '';
    document.getElementById('upload-file-nome').textContent =
        'Clique para selecionar o PDF';

    // Mostra campo de mês para folha de ponto e contracheque
    const campoMes = document.getElementById('campo-mes');
    if (categoria === 'FOLHA_PONTO' || categoria === 'CONTRACHEQUE') {
        campoMes.style.display = 'block';
    } else {
        campoMes.style.display = 'none';
    }

    document.getElementById('modal-upload').classList.add('active');
}

function fecharModalUpload() {
    document.getElementById('modal-upload').classList.remove('active');
}

function mostrarNomeArquivo(input) {
    const nome = input.files[0]?.name || 'Clique para selecionar o PDF';
    document.getElementById('upload-file-nome').textContent = nome;
}

// ===================================
// CRUD ARQUIVOS
// ===================================
async function salvarArquivo() {
    const nome = document.getElementById('upload-nome').value.trim();
    const mes  = document.getElementById('upload-mes').value;

    if (!nome) {
        alert('Informe o nome do arquivo.');
        return;
    }

    const data = {
        funcionarioId: parseInt(funcionarioId),
        categoria: categoriaAtual,
        nomeArquivo: nome,
        mesReferencia: mes || null
    };

    const resultado = await api.post('/arquivos', data);

    if (resultado) {
        fecharModalUpload();
        await carregarArquivos();
    } else {
        alert('Erro ao salvar. Tente novamente.');
    }
}

async function removerArquivo(id, categoria) {
    if (!confirm('Deseja remover este arquivo?')) return;
    const ok = await api.delete(`/arquivos/${id}`);
    if (ok) await carregarArquivos();
}

// ===================================
// UTILITÁRIOS LOCAIS
// ===================================
function formatarMes(mes) {
    if (!mes) return '—';
    const [ano, m] = mes.split('-');
    const meses = ['Jan','Fev','Mar','Abr','Mai','Jun',
                   'Jul','Ago','Set','Out','Nov','Dez'];
    return `${meses[parseInt(m) - 1]}/${ano}`;
}

function formatarDataHora(dt) {
    if (!dt) return '—';
    const d = new Date(dt);
    return d.toLocaleDateString('pt-BR') + ' ' +
           d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
}
