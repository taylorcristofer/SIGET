// ===================================
// CONTRATOS
// ===================================
let contratos = [];
let orgaos = [];

document.addEventListener("DOMContentLoaded", async () => {
  await carregarOrgaos();
  await carregarContratos();
});

async function carregarOrgaos() {
  orgaos = (await api.get("/orgaos")) || [];
  const select = document.getElementById("orgaoId");
  orgaos.forEach((o) => {
    const option = document.createElement("option");
    option.value = o.id;
    option.textContent = `${o.sigla ? o.sigla + " — " : ""}${o.nome}`;
    select.appendChild(option);
  });
}

async function carregarContratos() {
  contratos = (await api.get("/contratos/todos")) || [];
  renderizarTabela(contratos);
}

function renderizarTabela(lista) {
  const tbody = document.getElementById("tabela-contratos");

  if (lista.length === 0) {
    tbody.innerHTML = `
            <tr>
                <td colspan="8" class="text-center"
                    style="padding: 32px; color: #999;">
                    Nenhum contrato encontrado
                </td>
            </tr>`;
    return;
  }

  tbody.innerHTML = lista
    .map(
      (c) => `
        <tr>
            <td><strong>${c.numero}</strong></td>
            <td>
                ${c.orgaoSigla ? `<span class="badge badge-info">${c.orgaoSigla}</span> ` : ""}
                ${c.orgaoNome}
            </td>
            <td style="max-width: 200px; overflow: hidden;
                       text-overflow: ellipsis; white-space: nowrap;">
                ${c.objeto || "—"}
            </td>
            <td>${formatarData(c.dataInicio)}</td>
            <td>${formatarData(c.dataFim)}</td>
            <td>${
              c.valorMensal
                ? "R$ " +
                  Number(c.valorMensal).toLocaleString("pt-BR", {
                    minimumFractionDigits: 2,
                  })
                : "—"
            }</td>
            <td>${
              c.ativo
                ? '<span class="badge badge-success">Ativo</span>'
                : '<span class="badge badge-danger">Inativo</span>'
            }</td>
            <td>
                <div class="flex gap-8">
                    <button class="btn btn-outline btn-sm"
                        onclick="abrirModalEdicao(${c.id})">
                        ✏️ Editar
                    </button>
                    ${
                      c.arquivoPdf
                        ? `
                        <button class="btn btn-sm"
                            style="background:#e3f2fd;color:#1565c0;border:none;cursor:pointer;"
                            onclick="verPdf(${c.id})">
                            📄 PDF
                        </button>`
                        : ""
                    }
                    ${
                      c.ativo
                        ? `<button class="btn btn-sm"
                            style="background:#ffebee;color:#c62828;border:none;cursor:pointer;"
                            onclick="desativarContrato(${c.id})">
                            🚫 Desativar
                        </button>`
                        : `<button class="btn btn-sm"
                            style="background:#e8f5e9;color:#2e7d32;border:none;cursor:pointer;"
                            onclick="reativarContrato(${c.id})">
                            ✅ Reativar
                        </button>`
                    }
                </div>
            </td>
        </tr>
    `,
    )
    .join("");
}

function filtrarTabela() {
  const busca = document.getElementById("busca").value.toLowerCase();
  const filtrados = contratos.filter(
    (c) =>
      c.numero.toLowerCase().includes(busca) ||
      c.orgaoNome.toLowerCase().includes(busca) ||
      (c.objeto && c.objeto.toLowerCase().includes(busca)),
  );
  renderizarTabela(filtrados);
}

// ===================================
// MODAL
// ===================================
function abrirModalCadastro() {
  document.getElementById("modal-titulo").textContent = "Novo Contrato";
  document.getElementById("contrato-id").value = "";
  document.getElementById("numero").value = "";
  document.getElementById("orgaoId").value = "";
  document.getElementById("dataInicio").value = "";
  document.getElementById("dataFim").value = "";
  document.getElementById("valorMensal").value = "";
  document.getElementById("objeto").value = "";
  document.getElementById("arquivoPdf").value = "";
  document.getElementById("modal").classList.add("active");
}

function abrirModalEdicao(id) {
  const c = contratos.find((c) => c.id === id);
  if (!c) return;

  document.getElementById("modal-titulo").textContent = "Editar Contrato";
  document.getElementById("contrato-id").value = c.id;
  document.getElementById("numero").value = c.numero || "";
  document.getElementById("orgaoId").value = c.orgaoId || "";
  document.getElementById("dataInicio").value = c.dataInicio || "";
  document.getElementById("dataFim").value = c.dataFim || "";
  document.getElementById("valorMensal").value = c.valorMensal || "";
  document.getElementById("objeto").value = c.objeto || "";
  document.getElementById("arquivoPdf").value = "";
  document.getElementById("modal").classList.add("active");
}

function fecharModal() {
  document.getElementById("modal").classList.remove("active");
}

// ===================================
// CRUD
// ===================================
async function salvarContrato() {
  const id = document.getElementById("contrato-id").value;
  const arquivoInput = document.getElementById("arquivoPdf");
  const arquivo = arquivoInput.files[0];

  const data = {
    numero: document.getElementById("numero").value,
    orgaoId: document.getElementById("orgaoId").value,
    dataInicio: document.getElementById("dataInicio").value,
    dataFim: document.getElementById("dataFim").value || null,
    valorMensal: document.getElementById("valorMensal").value || null,
    objeto: document.getElementById("objeto").value,
  };

  if (!data.numero || !data.orgaoId || !data.dataInicio) {
    alert("Preencha os campos obrigatórios: Número, Órgão e Data de Início.");
    return;
  }

  let resultado;
  if (id) {
    resultado = await api.put(`/contratos/${id}`, data);
  } else {
    resultado = await api.post("/contratos", data);
  }

  if (resultado && arquivo) {
    await uploadPdf(resultado.id, arquivo);
  }

  if (resultado) {
    fecharModal();
    await carregarContratos();
  } else {
    alert("Erro ao salvar. Verifique os dados e tente novamente.");
  }
}

async function uploadPdf(contratoId, arquivo) {
  const formData = new FormData();
  formData.append("arquivo", arquivo);

  try {
    await fetch(`http://localhost:8080/api/contratos/${contratoId}/upload`, {
      method: "POST",
      body: formData,
    });
  } catch (error) {
    console.error("Erro no upload do PDF:", error);
  }
}

async function verPdf(contratoId) {
  window.open(
    `http://localhost:8080/api/contratos/${contratoId}/pdf`,
    "_blank",
  );
}

async function desativarContrato(id) {
  if (!confirm("Deseja desativar este contrato?")) return;
  const ok = await api.delete(`/contratos/${id}`);
  if (ok) await carregarContratos();
}

async function reativarContrato(id) {
  const c = contratos.find((c) => c.id === id);
  if (!c) return;
  const data = { ...c, ativo: true };
  const ok = await api.put(`/contratos/${id}`, data);
  if (ok) await carregarContratos();
}
