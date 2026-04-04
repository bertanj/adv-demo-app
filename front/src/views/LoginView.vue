<template>
  <div class="min-h-screen w-full flex items-center justify-center bg-slate-100 p-6">
    <div class="max-w-[1000px] w-full grid grid-cols-1 md:grid-cols-2 bg-white rounded-2xl shadow-2xl overflow-hidden">
      <div class="hidden md:flex flex-col justify-between p-12 bg-[#1e293b] text-white">
        <div>
          <div class="flex items-center gap-3 mb-16">
            <span class="material-symbols-outlined text-orange-500 text-4xl">WISE</span>
            <h1 class="text-3xl font-bold">LegalSystem</h1>
          </div>
          <h2 class="text-5xl font-extrabold leading-tight">Gestão jurídica inteligente.</h2>
        </div>
      </div>

      <div class="p-12 flex flex-col justify-center">
        <h2 class="text-3xl font-bold mb-2">Bem-vindo</h2>
        <p class="text-slate-500 mb-8">Acesse sua conta para continuar.</p>
        
        <!-- Error Banner -->
        <div v-if="errorMessage" class="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg mb-4 text-sm">
          {{ errorMessage }}
        </div>

        <form @submit.prevent="handleLogin" class="space-y-6">
          <input type="email" v-model="email" placeholder="E-mail" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          <input type="password" v-model="password" placeholder="Senha" class="w-full p-3 border rounded-lg outline-none focus:ring-2 focus:ring-orange-500">
          <button type="submit" class="w-full py-4 bg-[#1e293b] text-white font-bold rounded-lg hover:bg-slate-800 transition-all">
            Entrar no Sistema
          </button>
        </form>

        <div class="mt-8 text-center border-t pt-6">
          <p class="text-slate-500">Ainda não tem uma conta? <router-link to="/register" class="text-orange-600 font-bold hover:underline">Cadastre-se</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'LoginView',
  data() {
    return {
      email: '',
      password: '',
      errorMessage: ''
    }
  },
  methods: {
    async handleLogin() {
      this.errorMessage = '';
      try {
        const response = await api.instance.post('/login', { 
          username: this.email, 
          password: this.password 
        });
        // Salva token JWT e dados do usuário
        localStorage.setItem('auth_user', JSON.stringify(response.data));
        this.$router.push('/dashboard');
      } catch (error) {
        console.error("Erro no login", error);
        if (error.response?.status === 401) {
          this.errorMessage = 'E-mail ou senha incorretos.';
        } else {
          this.errorMessage = 'Erro ao conectar com o servidor.';
        }
      }
    }
  }
}
</script>