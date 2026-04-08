<template>
  <div class="p-8">
    <div class="flex justify-between items-center mb-8">
      <div>
        <h2 class="text-3xl font-bold">Assistentes</h2>
        <p class="text-slate-500">Gerencie os assistentes do escritorio.</p>
      </div>
      <button @click="showForm ? limparForm() : (showForm = true)" class="bg-[#ec5b13] text-white px-6 py-2.5 rounded-xl font-bold shadow-lg shadow-orange-200">
        {{ showForm ? 'Cancelar' : '+ Novo Assistente' }}
      </button>
    </div>

    <div v-if="showForm" class="bg-white p-6 rounded-xl shadow-sm mb-8 border border-slate-200">
      <h3 class="text-xl font-bold mb-4">{{ editandoId ? 'Editar Assistente' : 'Novo Assistente' }}</h3>
      <form @submit.prevent="salvarAssistente" class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-semibold mb-1">Nome Completo</label>
          <input v-model="formData.name" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="Nome completo" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">CPF</label>
          <input v-model="formData.cpf" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="000.000.000-00" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">E-mail</label>
          <input v-model="formData.email" type="email" required class="w-full border rounded-lg px-4 py-2" placeholder="email@exemplo.com" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">Senha</label>
          <input v-model="formData.password" type="password" :required="!editandoId" class="w-full border rounded-lg px-4 py-2" :placeholder="editandoId ? 'Deixe vazio para manter' : 'Senha'" />
        </div>
        <div class="md:col-span-2 flex justify-end">
          <button type="submit" class="bg-blue-600 text-white px-8 py-2 rounded-lg font-bold">
            {{ editandoId ? 'Atualizar' : 'Cadastrar' }}
          </button>
        </div>
      </form>
    </div>

    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
      <table class="w-full text-left">
        <thead class="bg-slate-50 text-slate-500 text-[11px] uppercase font-bold tracking-widest">
          <tr>
            <th class="px-8 py-5">Nome</th>
            <th class="px-8 py-5">E-mail</th>
            <th class="px-8 py-5">CPF</th>
            <th class="px-8 py-5 text-right">Acoes</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-for="a in assistentes" :key="a.id" class="hover:bg-slate-50 transition-colors">
            <td class="px-8 py-5 font-bold text-slate-900">{{ a.name }}</td>
            <td class="px-8 py-5 text-sm text-slate-600">{{ a.email }}</td>
            <td class="px-8 py-5 text-sm text-slate-600">{{ a.cpf }}</td>
            <td class="px-8 py-5 text-right">
              <button @click="editarAssistente(a)" class="text-blue-600 font-bold text-sm hover:underline mr-3">Editar</button>
              <button @click="excluirAssistente(a.id)" class="text-red-600 font-bold text-sm hover:underline">Excluir</button>
            </td>
          </tr>
          <tr v-if="assistentes.length === 0">
            <td colspan="4" class="p-10 text-center text-slate-400">Nenhum assistente cadastrado.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'AssistentesView',
  data() {
    return {
      assistentes: [],
      showForm: false,
      editandoId: null,
      formData: {
        name: '',
        cpf: '',
        email: '',
        password: ''
      }
    }
  },
  mounted() {
    this.carregarAssistentes();
  },
  methods: {
    async carregarAssistentes() {
      try {
        const response = await api.assistentes.getAll();
        this.assistentes = response.data;
      } catch (e) {
        console.error("Erro ao carregar assistentes", e);
      }
    },
    editarAssistente(a) {
      this.editandoId = a.id;
      this.formData = {
        name: a.name || '',
        cpf: a.cpf || '',
        email: a.email || '',
        password: ''
      };
      this.showForm = true;
    },
    limparForm() {
      this.editandoId = null;
      this.formData = { name: '', cpf: '', email: '', password: '' };
      this.showForm = false;
    },
    async salvarAssistente() {
      try {
        const payload = {
          name: this.formData.name,
          cpf: this.formData.cpf,
          email: this.formData.email,
          username: this.formData.email,
          password: this.formData.password || 'TempPass123'
        };
        let response;
        if (this.editandoId) {
          response = await api.assistentes.update(this.editandoId, payload);
        } else {
          response = await api.assistentes.create(payload);
        }
        if (response.status === 200 || response.status === 201) {
          this.limparForm();
          this.carregarAssistentes();
        }
      } catch (e) {
        console.error("Erro ao salvar assistente", e);
        alert("Erro: " + (e.response?.data?.message || e.message));
      }
    },
    async excluirAssistente(id) {
      if (confirm('Tem certeza que deseja excluir este assistente?')) {
        try {
          await api.assistentes.delete(id);
          this.carregarAssistentes();
        } catch (e) {
          console.error("Erro ao excluir", e);
        }
      }
    }
  }
}
</script>
