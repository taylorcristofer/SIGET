// ===================================
// DASHBOARD
// ===================================
document.addEventListener('DOMContentLoaded', async () => {
    await carregarEstatisticas();
    await carregarAlocacoesRecentes();
    await carregarFuncionariosRecentes();
});

async function carregarEstatisticas() {
    const [funcionarios, orgaos, contratos, alocacoes] = await Promise.all([
        api.get('/funcionarios'),
        api.get('/orgaos'),
        api.get('/contratos'),
        api.get('/alocacoes')
    ]);

    document.getElementById('total-funcionarios').textContent =
        funcionarios ? funcionarios.length : '0';
    document.getElementById('total-orgaos').textContent =
        orgaos ? orgaos.length : '0';
    document.getElementById('total-contratos').textContent =
        contratos ? contratos.length : '0';
    document.getElementById('total-alocacoes').textContent =
        alocacoes ? alocacoes.length : '0';
}

async function carregarAlocacoesRecentes() {
    const alocacoes = await api.get('/alocacoes');
    const tbody = document.getElementById('tabela-alocacoes');

    if (!alocacoes || alocacoes.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center"
                    style="padding: 32px; color: #999;">
                    Nenhuma alocação encontrada
                </td>
            </tr>`;
        return;
    }

    const recentes = alocacoes.slice(0, 5);
    tbody.innerHTML = recentes.map(a => `
        <tr>
            <td><strong>${a.funcionarioNome}</strong></td>
            <td>${a.funcionarioFuncao}</td>
            <td>${a.orgaoSigla} — ${a.orgaoNome}</td>
            <td>${a.contratoNumero}</td>
            <td>${badgeTurno(a.turno)}</td>
            <td>${badgeStatus(a.status)}</td>
        </tr>
    `).join('');
}

async function carregarFuncionariosRecentes() {
    const funcionarios = await api.get('/funcionarios');
    const tbody = document.getElementById('tabela-funcionarios');

    if (!funcionarios || funcionarios.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center"
                    style="padding: 32px; color: #999;">
                    Nenhum funcionário encontrado
                </td>
            </tr>`;
        return;
    }

    const recentes = funcionarios.slice(0, 5);
    tbody.innerHTML = recentes.map(f => `
        <tr>
            <td><strong>${f.nome}</strong></td>
            <td>${formatarCPF(f.cpf)}</td>
            <td>${f.funcao}</td>
            <td>${badgeStatus(f.status)}</td>
        </tr>
    `).join('');
}