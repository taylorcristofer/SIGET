// ===================================
// CONFIGURAÇÃO CENTRAL DA API
// ===================================
const API_BASE = 'http://localhost:8080/api';

const api = {

    async get(endpoint) {
        try {
            const response = await fetch(`${API_BASE}${endpoint}`);
            if (!response.ok) throw new Error(`Erro ${response.status}`);
            return await response.json();
        } catch (error) {
            console.error(`GET ${endpoint}:`, error);
            return null;
        }
    },

    async post(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE}${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error(`Erro ${response.status}`);
            return await response.json();
        } catch (error) {
            console.error(`POST ${endpoint}:`, error);
            return null;
        }
    },

    async put(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE}${endpoint}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error(`Erro ${response.status}`);
            return await response.json();
        } catch (error) {
            console.error(`PUT ${endpoint}:`, error);
            return null;
        }
    },

    async delete(endpoint) {
        try {
            const response = await fetch(`${API_BASE}${endpoint}`, {
                method: 'DELETE'
            });
            return response.ok;
        } catch (error) {
            console.error(`DELETE ${endpoint}:`, error);
            return false;
        }
    }
};

// ===================================
// FUNÇÕES UTILITÁRIAS
// ===================================
function formatarData(data) {
    if (!data) return '—';
    const [ano, mes, dia] = data.split('-');
    return `${dia}/${mes}/${ano}`;
}

function formatarCPF(cpf) {
    if (!cpf) return '—';
    return cpf;
}

function badgeStatus(status) {
    const map = {
        'ATIVO':      '<span class="badge badge-success">Ativo</span>',
        'INATIVO':    '<span class="badge badge-danger">Inativo</span>',
        'ATIVA':      '<span class="badge badge-success">Ativa</span>',
        'ENCERRADA':  '<span class="badge badge-danger">Encerrada</span>',
        'SUSPENSA':   '<span class="badge badge-warning">Suspensa</span>',
    };
    return map[status] || `<span class="badge badge-info">${status}</span>`;
}

function badgeTurno(turno) {
    const map = {
        'MANHA':  '<span class="badge badge-info">Manhã</span>',
        'TARDE':  '<span class="badge badge-warning">Tarde</span>',
        'NOITE':  '<span class="badge" style="background:#ede7f6;color:#4527a0;">Noite</span>',
    };
    return map[turno] || `<span class="badge badge-info">${turno || '—'}</span>`;
}