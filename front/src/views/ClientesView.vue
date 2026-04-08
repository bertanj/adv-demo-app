<template>
  <div class="p-8">
    <div class="flex justify-between items-center mb-8">
      <h2 class="text-3xl font-bold">Gestão de Clientes</h2>
      <button @click="showForm ? limparForm() : (showForm = true)" class="bg-orange-600 text-white px-6 py-2 rounded-lg font-bold">
        {{ showForm ? 'Cancelar' : '+ Novo Cliente' }}
      </button>
    </div>

    <!-- Novo Cliente Form -->
    <div v-if="showForm" class="bg-white p-6 rounded-xl shadow-sm mb-8 border border-slate-200">
      <h3 class="text-xl font-bold mb-4">{{ editandoId ? 'Editar Cliente' : 'Adicionar Novo Cliente' }}</h3>
      <form @submit.prevent="salvarCliente" class="flex gap-4 items-end">
        <div class="flex-1">
          <label class="block text-sm font-semibold mb-1">Nome Completo</label>
          <input v-model="formData.name" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="Nome do cliente" />
        </div>
        <div class="flex-1">
          <label class="block text-sm font-semibold mb-1">CPF</label>
          <input v-model="formData.cpf" type="text" required class="w-full border rounded-lg px-4 py-2" placeholder="000.000.000-00" />
        </div>
        <button type="submit" class="bg-blue-600 text-white px-6 py-2 rounded-lg font-bold h-[42px]">{{ editandoId ? 'Atualizar' : 'Salvar' }}</button>
      </form>
    </div>

    <table class="w-full bg-white rounded-xl shadow-sm overflow-hidden">
      <thead class="bg-slate-50 border-b">
        <tr>
          <th class="p-4 text-left">Nome</th>
          <th class="p-4 text-left">CPF</th>
          <th class="p-4 text-center">Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="cliente in clientes" :key="cliente.id" class="border-b hover:bg-slate-50">
          <td class="p-4 font-semibold">{{ cliente.name }}</td>
          <td class="p-4 text-slate-500">{{ cliente.cpf }}</td>
          <td class="p-4 text-center">
            <button @click="editarCliente(cliente)" class="text-blue-800 font-bold mr-4">Editar</button>
            <button @click="excluirCliente(cliente.id)" class="text-red-600 font-bold">Excluir</button>
          </td>
        </tr>
        <tr v-if="clientes.length === 0">
          <td colspan="3" class="p-10 text-center text-slate-400">Nenhum cliente no banco de dados.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'ClientesView',
  data() { 
    return { 
      clientes: [],
      showForm: false,
      editandoId: null,
      formData: {
        name: '',
        cpf: ''
      }
    } 
  },
  mounted() { 
    this.carregarClientes(); 
  },
  methods: {
    async carregarClientes() {
      try {
        const response = await api.clientes.getAll();
        this.clientes = response.data;
      } catch (e) { 
        console.error("Erro ao carregar clientes", e); 
      }
    },
    editarCliente(cliente) {
      this.editandoId = cliente.id;
      this.formData = {
        name: cliente.name || '',
        cpf: cliente.cpf || ''
      };
      this.showForm = true;
    },
    limparForm() {
      this.editandoId = null;
      this.formData = { name: '', cpf: '' };
      this.showForm = false;
    },
    async salvarCliente() {
      try {
        const payload = {
            name: this.formData.name,
            cpf: this.formData.cpf,
            username: null,
            email: null,
            password: null,
            advogado: null
        };
        let response;
        if (this.editandoId) {
          response = await api.clientes.update(this.editandoId, payload);
        } else {
          response = await api.clientes.create(payload);
        }
        if (response.status === 201 || response.status === 200) {
          this.limparForm();
          this.carregarClientes();
        }
      } catch (e) {
        console.error("Erro ao salvar cliente", e);
        alert("Erro de comunicação com o servidor: " + (e.response?.data?.message || e.message));
      }
    },
    async excluirCliente(id) {
      if (confirm('Tem certeza que deseja excluir?')) {
        try {
          await api.clientes.delete(id);
          this.carregarClientes();
        } catch (e) {
          console.error("Erro ao excluir", e);
        }
      }
    }
  }
}
</script>