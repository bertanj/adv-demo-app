<template>
  <div class="min-h-screen w-full flex items-center justify-center bg-slate-100 p-6">
    <div class="max-w-[1000px] w-full grid grid-cols-1 md:grid-cols-2 bg-white rounded-2xl shadow-2xl overflow-hidden">
      <!-- Left Panel -->
      <div class="hidden md:flex flex-col justify-between p-12 bg-[#1e293b] text-white">
        <div>
          <div class="flex items-center gap-3 mb-16">
            <span class="material-symbols-outlined text-orange-500 text-4xl">WISE</span>
            <h1 class="text-3xl font-bold">LegalSystem</h1>
          </div>
          <h2 class="text-5xl font-extrabold leading-tight">Faça parte do futuro.</h2>
          <p class="text-slate-400 mt-6 text-lg">Crie sua conta para começar a gerenciar seus processos de forma inteligente e rápida.</p>
        </div>
      </div>

      <!-- Right Panel -->
      <div class="p-12 flex flex-col justify-center">
        <h2 class="text-3xl font-bold mb-2">Crie sua conta</h2>
        <p class="text-slate-500 mb-8">Preencha os dados abaixo para se cadastrar.</p>
        
        <!-- Error Banner -->
        <div v-if="errorMessage" class="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg mb-6 flex items-center gap-3">
          <span class="material-symbols-outlined">error</span>
          <span>{{ errorMessage }}</span>
        </div>

        <form @submit.prevent="handleRegister" class="space-y-4">
          <input type="text" v-model="name" required placeholder="Nome Completo" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          <input type="text" v-model="cpf" required placeholder="CPF" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          <input type="email" v-model="email" required placeholder="E-mail" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          <input type="text" v-model="oab" required placeholder="OAB" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          <input type="password" v-model="password" required placeholder="Senha" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          
          <button 
            type="submit" 
            :disabled="loading"
            class="w-full py-4 mt-2 bg-[#1e293b] text-white font-bold rounded-lg transition-all flex justify-center items-center gap-2"
            :class="loading ? 'opacity-70 cursor-not-allowed' : 'hover:bg-slate-800'"
          >
            <span v-if="loading" class="material-symbols-outlined animate-spin">progress_activity</span>
            {{ loading ? 'Criando Conta...' : 'Criar Conta' }}
          </button>
        </form>

        <div class="mt-8 text-center">
          <p class="text-slate-500">Já possui uma conta? <router-link to="/" class="text-orange-600 font-bold hover:underline">Faça Login</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'RegisterView',
  data() {
    return {
      name: '',
      cpf: '',
      email: '',
      oab: '',
      password: '',
      loading: false,
      errorMessage: ''
    }
  },
  methods: {
    async handleRegister() {
      this.loading = true;
      this.errorMessage = '';
      try {
        const payload = {
          name: this.name,
          cpf: this.cpf,
          email: this.email,
          username: this.email,
          oab: this.oab,
          password: this.password
        };
        const response = await api.advogados.create(payload);
        
        if (response.status === 201 || response.status === 200) {
          alert('Conta criada com sucesso! Faça o login para continuar.');
          this.$router.push('/');
        }
      } catch (error) {
        console.error("Erro no cadastro", error);
        
        let msg = 'Ocorreu um erro ao comunicar com o servidor. Tente novamente. (' + error.message + ')';
        if (error.response?.status === 500) {
            msg = 'Este CPF ou E-mail já está cadastrado no sistema.';
        } else if (error.response?.data?.message) {
            msg = error.response.data.message;
        } else if (error.response) {
            msg = `Servidor retornou erro ${error.response.status}: ` + JSON.stringify(error.response.data);
        }
        
        this.errorMessage = msg;
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>
