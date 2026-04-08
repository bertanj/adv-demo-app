<template>
  <div class="p-8">
    <div class="flex justify-between items-center mb-8">
      <div>
        <h2 class="text-3xl font-bold">Processos</h2>
        <p class="text-slate-500">Administre os trâmites jurídicos e acompanhe atualizações.</p>
      </div>
      <button @click="showForm ? limparForm() : (showForm = true)" class="bg-[#ec5b13] text-white px-6 py-2.5 rounded-xl font-bold shadow-lg shadow-orange-200">
        {{ showForm ? 'Cancelar' : '+ Novo Processo' }}
      </button>
    </div>

    <!-- Novo Processo Form -->
    <div v-if="showForm" class="bg-white p-6 rounded-xl shadow-sm mb-8 border border-slate-200">
      <h3 class="text-xl font-bold mb-4">{{ editandoId ? 'Editar Processo' : 'Adicionar Novo Processo' }}</h3>
      <form @submit.prevent="salvarProcesso" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-semibold mb-1">Número Processo (CNJ)</label>
          <input v-model="formData.numeroProcesso" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="0000000-00.0000.0.00.0000" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">Título / Causa</label>
          <input v-model="formData.titulo" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="Indenizatória - Cliente vs Empresa" />
        </div>
         <div>
          <label class="block text-sm font-semibold mb-1">Status</label>
          <select v-model="formData.status" required class="w-full border rounded-lg px-4 py-2">
            <option value="" disabled>Selecione o status</option>
            <option value="Ativo">Ativo</option>
            <option value="Em Recurso">Em Recurso</option>
            <option value="Finalizado">Finalizado</option>
            <option value="Inativo">Inativo</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">Tribunal</label>
          <input v-model="formData.tribunal" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="Ex: TJPR, TRF4" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">Vara</label>
          <input v-model="formData.vara" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="Ex: 2ª Vara Cível" />
        </div>
        <div class="lg:col-span-3">
          <label class="block text-sm font-semibold mb-1">Descrição</label>
          <textarea v-model="formData.descricao" rows="2" class="w-full border rounded-lg px-4 py-2" placeholder="Detalhes da causa..."></textarea>
        </div>
        <div class="lg:col-span-3">
          <label class="block text-sm font-semibold mb-1">Assistentes Designados</label>
          <div v-if="assistentesDisponiveis.length === 0" class="text-sm text-slate-400">Nenhum assistente cadastrado.</div>
          <div v-else class="flex flex-wrap gap-3">
            <label v-for="a in assistentesDisponiveis" :key="a.id" class="flex items-center gap-2 bg-slate-50 px-3 py-2 rounded-lg border cursor-pointer hover:bg-slate-100">
              <input type="checkbox" :value="a.id" v-model="formData.assistentesIds" class="accent-blue-600" />
              <span class="text-sm">{{ a.name }}</span>
            </label>
          </div>
        </div>
        <div class="lg:col-span-3 flex justify-end">
          <button type="submit" class="bg-blue-600 text-white px-8 py-2 rounded-lg font-bold">{{ editandoId ? 'Atualizar Processo' : 'Salvar Processo' }}</button>
        </div>
      </form>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
      <table class="w-full text-left">
        <thead class="bg-slate-50 text-slate-500 text-[11px] uppercase font-bold tracking-widest">
          <tr>
            <th class="px-8 py-5">Nº Processo & Tribunal</th>
            <th class="px-8 py-5">Título / Causa</th>
            <th class="px-8 py-5 text-center">Status</th>
            <th class="px-8 py-5 text-right">Ações</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-for="processo in processos" :key="processo.id" class="hover:bg-slate-50 transition-colors group">
            <td class="px-8 py-5">
              <p class="font-bold text-slate-900">{{ processo.numeroProcesso }}</p>
              <p class="text-xs text-slate-400">{{ processo.tribunal }} • {{ processo.vara }}</p>
            </td>
            <td class="px-8 py-5">
              <p class="text-sm font-semibold">{{ processo.titulo || 'N/A' }}</p>
            </td>
            <td class="px-8 py-5 text-center">
              <span class="px-3 py-1.5 bg-amber-100 text-amber-700 text-[10px] font-bold rounded-lg uppercase">
                {{ processo.status || 'Ativo' }}
              </span>
            </td>
            <td class="px-8 py-5 text-right space-x-3">
              <button @click="editarProcesso(processo)" class="text-blue-600 font-bold text-sm hover:underline">Editar</button>
              <button @click="excluirProcesso(processo.id)" class="text-red-600 font-bold text-sm hover:underline">Excluir</button>
            </td>
          </tr>
          <tr v-if="processos.length === 0">
            <td colspan="4" class="p-10 text-center text-slate-400">Nenhum processo encontrado no banco de dados.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'ProcessosView',
  data() {
    return {
      processos: [],
      assistentesDisponiveis: [],
      showForm: false,
      editandoId: null,
      formData: {
        numeroProcesso: '',
        titulo: '',
        descricao: '',
        tribunal: '',
        vara: '',
        status: '',
        assistentesIds: []
      }
    }
  },
  mounted() {
    this.carregarProcessos();
    this.carregarAssistentes();
  },
  methods: {
    async carregarAssistentes() {
      try {
        const response = await api.assistentes.getAll();
        this.assistentesDisponiveis = response.data;
      } catch (e) {
        console.error("Erro ao carregar assistentes", e);
      }
    },
    async carregarProcessos() {
      try {
        const response = await api.processos.getAll();
        this.processos = response.data;
      } catch (e) {
        console.error("Erro ao carregar processos", e);
      }
    },
    editarProcesso(processo) {
      this.editandoId = processo.id;
      this.formData = {
        numeroProcesso: processo.numeroProcesso,
        titulo: processo.titulo || '',
        descricao: processo.descricao || '',
        tribunal: processo.tribunal || '',
        vara: processo.vara || '',
        status: processo.status || '',
        assistentesIds: (processo.assistentesDesignados || []).map(a => a.id)
      };
      this.showForm = true;
    },
    limparForm() {
      this.editandoId = null;
      this.formData = { numeroProcesso: '', titulo: '', descricao: '', tribunal: '', vara: '', status: '', assistentesIds: [] };
      this.showForm = false;
    },
    async salvarProcesso() {
      try {
         const payload = {
            numeroProcesso: this.formData.numeroProcesso,
            titulo: this.formData.titulo,
            descricao: this.formData.descricao,
            tribunal: this.formData.tribunal,
            vara: this.formData.vara,
            status: this.formData.status,
            assistentesDesignadosIds: this.formData.assistentesIds
         };
         let response;
         if (this.editandoId) {
            response = await api.processos.update(this.editandoId, payload);
         } else {
            response = await api.processos.create(payload);
         }
         if(response.status === 201 || response.status === 200) {
            this.limparForm();
            this.carregarProcessos();
         }
      } catch (e) {
        console.error("Erro ao salvar processo: ", e);
        alert("Erro de comunicação com o servidor: " + (e.response?.data?.message || e.message));
      }
    },
    async excluirProcesso(id) {
       if (confirm('Tem certeza que deseja excluir este processo?')) {
        try {
          await api.processos.delete(id);
          this.carregarProcessos();
        } catch (e) {
          console.error("Erro ao excluir", e);
        }
      }
    }
  }
}
</script>