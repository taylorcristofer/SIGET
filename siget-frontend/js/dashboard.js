document.addEventListener('DOMContentLoaded', async () => {
    await carregarEstatisticas();
    await carregarFuncionariosEmFerias();
    await carregarFuncionariosRecentes();
});

async function carregarEstatisticas() {
    const [funcionarios, orgaos, contratos, ferias] = await Promise.all([
        api.get('/funcionarios'),
        api.get('/orgaos'),
        api.get('/contratos'),
        api.get('/ferias/em-ferias')
    ]);

    document.getElementById('total-funcionarios').textContent =
        funcionarios ? funcionarios.length : '0';
    document.getElementById('total-orgaos').textContent =
        orgaos ? orgaos.length : '0';
    document.getElementById('total-contratos').textContent =
        contratos ? contratos.length : '0';
    document.getElementById('total-ferias').textContent =
        ferias ? ferias.length : '0';
}

async function carregarFuncionariosEmFerias() {
    const ferias = await api.get('/ferias/em-ferias');
    const tbody = document.getElementById('tabela-ferias');

    if (!ferias || ferias.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center"
                    style="padding: 32px; color: #999;">
                    Nenhum funcionário de férias no momento
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = ferias.map(f => `
        <tr>
            <td><strong>${f.nomeFuncionario}</strong></td>
            <td>${formatarData(f.dataInicio)}</td>
            <td>${formatarData(f.dataFim)}</td>
            <td>${f.diasRestantes} dias restantes</td>
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