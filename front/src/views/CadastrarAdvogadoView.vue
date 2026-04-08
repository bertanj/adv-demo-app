<template>
  <div class="p-8">
    <div class="flex justify-between items-center mb-8">
      <div>
        <h2 class="text-3xl font-bold">Gerenciar Advogados</h2>
        <p class="text-slate-500">Cadastre e gerencie advogados do escritorio.</p>
      </div>
      <button @click="showForm ? limparForm() : (showForm = true)" class="bg-[#ec5b13] text-white px-6 py-2.5 rounded-xl font-bold shadow-lg shadow-orange-200">
        {{ showForm ? 'Cancelar' : '+ Novo Advogado' }}
      </button>
    </div>

    <div v-if="successMessage" class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg mb-6 flex items-center gap-3">
      <span class="material-symbols-outlined">check_circle</span>
      <span>{{ successMessage }}</span>
    </div>

    <div v-if="errorMessage" class="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg mb-6 flex items-center gap-3">
      <span class="material-symbols-outlined">error</span>
      <span>{{ errorMessage }}</span>
    </div>

    <div v-if="showForm" class="bg-white p-6 rounded-xl shadow-sm mb-8 border border-slate-200">
      <h3 class="text-xl font-bold mb-4">{{ editandoId ? 'Editar Advogado' : 'Cadastrar Novo Advogado' }}</h3>
      <form @submit.prevent="salvarAdvogado" class="grid grid-cols-1 md:grid-cols-2 gap-4">
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
          <label class="block text-sm font-semibold mb-1">OAB</label>
          <input v-model="formData.oab" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="OAB/PR 00000" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">Senha</label>
          <input v-model="formData.password" type="password" :required="!editandoId" class="w-full border rounded-lg px-4 py-2" :placeholder="editandoId ? 'Deixe vazio para manter a atual' : 'Senha inicial'" />
        </div>
        <div>
          <label class="block text-sm font-semibold mb-1">Perfil</label>
          <select v-model="formData.role" class="w-full border rounded-lg px-4 py-2">
            <option value="ADVOGADO">Advogado</option>
            <option value="ADMIN">Administrador</option>
          </select>
        </div>
        <div class="md:col-span-2 flex justify-end">
          <button type="submit" :disabled="loading" class="bg-blue-600 text-white px-8 py-2 rounded-lg font-bold">
            {{ loading ? 'Salvando...' : (editandoId ? 'Atualizar Advogado' : 'Cadastrar Advogado') }}
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
            <th class="px-8 py-5">OAB</th>
            <th class="px-8 py-5 text-center">Perfil</th>
            <th class="px-8 py-5 text-right">Acoes</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-for="adv in advogados" :key="adv.id" class="hover:bg-slate-50 transition-colors">
            <td class="px-8 py-5 font-bold text-slate-900">{{ adv.name }}</td>
            <td class="px-8 py-5 text-sm text-slate-600">{{ adv.email }}</td>
            <td class="px-8 py-5 text-sm text-slate-600">{{ adv.oab }}</td>
            <td class="px-8 py-5 text-center">
              <span :class="adv.role === 'ADMIN'
                ? 'bg-purple-100 text-purple-700'
                : 'bg-blue-100 text-blue-700'"
                class="px-3 py-1.5 text-[10px] font-bold rounded-lg uppercase">
                {{ adv.role }}
              </span>
            </td>
            <td class="px-8 py-5 text-right">
              <button @click="editarAdvogado(adv)" class="text-blue-600 font-bold text-sm hover:underline mr-3">Editar</button>
              <button @click="excluirAdvogado(adv.id)" class="text-red-600 font-bold text-sm hover:underline">Excluir</button>
            </td>
          </tr>
          <tr v-if="advogados.length === 0">
            <td colspan="5" class="p-10 text-center text-slate-400">Nenhum advogado cadastrado.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'CadastrarAdvogadoView',
  data() {
    return {
      advogados: [],
      showForm: false,
      editandoId: null,
      loading: false,
      successMessage: '',
      errorMessage: '',
      formData: {
        name: '',
        cpf: '',
        email: '',
        oab: '',
        password: '',
        role: 'ADVOGADO'
      }
    }
  },
  mounted() {
    this.carregarAdvogados();
  },
  methods: {
    async carregarAdvogados() {
      try {
        const response = await api.advogados.getAll();
        this.advogados = response.data;
      } catch (e) {
        console.error("Erro ao carregar advogados", e);
      }
    },
    editarAdvogado(adv) {
      this.editandoId = adv.id;
      this.formData = {
        name: adv.name || '',
        cpf: adv.cpf || '',
        email: adv.email || '',
        oab: adv.oab || '',
        password: '',
        role: adv.role || 'ADVOGADO'
      };
      this.showForm = true;
    },
    limparForm() {
      this.editandoId = null;
      this.formData = { name: '', cpf: '', email: '', oab: '', password: '', role: 'ADVOGADO' };
      this.showForm = false;
    },
    async salvarAdvogado() {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';
      try {
        const payload = {
          name: this.formData.name,
          cpf: this.formData.cpf,
          email: this.formData.email,
          username: this.formData.email,
          oab: this.formData.oab,
          password: this.formData.password,
          role: this.formData.role
        };
        let response;
        if (this.editandoId) {
          response = await api.advogados.update(this.editandoId, payload);
          this.successMessage = 'Advogado atualizado com sucesso!';
        } else {
          response = await api.advogados.createAsAdmin(payload);
          this.successMessage = 'Advogado cadastrado com sucesso!';
        }
        if (response.status === 201 || response.status === 200) {
          this.limparForm();
          this.carregarAdvogados();
        }
      } catch (e) {
        console.error("Erro ao cadastrar advogado", e);
        if (e.response?.status === 500) {
          this.errorMessage = 'Este CPF ou E-mail ja esta cadastrado.';
        } else if (e.response?.status === 403) {
          this.errorMessage = 'Voce nao tem permissao para esta acao.';
        } else {
          this.errorMessage = 'Erro ao cadastrar: ' + (e.response?.data?.message || e.message);
        }
      } finally {
        this.loading = false;
      }
    },
    async excluirAdvogado(id) {
      if (confirm('Tem certeza que deseja excluir este advogado?')) {
        try {
          await api.advogados.delete(id);
          this.carregarAdvogados();
        } catch (e) {
          console.error("Erro ao excluir", e);
        }
      }
    }
  }
}
</script>
