<template>
  <div class="p-8 bg-[#f8fafd] min-h-screen">
    <div class="mb-10">
      <h2 class="text-3xl font-bold text-slate-900">{{ saudacao }}, Dr. {{ userName }}</h2>
      <p class="text-slate-500 mt-1">Bem-vindo ao seu painel de controle.</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 mb-10">
      <div class="stat-card">
        <div class="mt-4">
          <p class="text-slate-500 text-sm font-bold uppercase">Processos Ativos</p>
          <p class="text-4xl font-extrabold text-slate-900">{{ processosAtivos }}</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="mt-4">
          <p class="text-slate-500 text-sm font-bold uppercase">Total de Processos</p>
          <p class="text-4xl font-extrabold text-slate-900">{{ totalProcessos }}</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="mt-4">
          <p class="text-slate-500 text-sm font-bold uppercase">Total de Clientes</p>
          <p class="text-4xl font-extrabold text-slate-900">{{ totalClientes }}</p>
        </div>
      </div>

      <div class="stat-card bg-gradient-to-br from-blue-900 to-indigo-900 text-white">
        <span class="material-symbols-outlined text-white bg-white/20 p-3 rounded-xl">payments</span>
        <div class="mt-4">
          <p class="text-blue-200 text-sm font-bold uppercase">OAB</p>
          <p class="text-2xl font-extrabold">{{ userOab || 'N/A' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { api } from '../services/api';

export default {
  name: 'DashboardView',
  data() {
    return {
      userName: '',
      userOab: '',
      totalProcessos: 0,
      processosAtivos: 0,
      totalClientes: 0
    }
  },
  computed: {
    saudacao() {
      const hora = new Date().getHours();
      if (hora < 12) return 'Bom dia';
      if (hora < 18) return 'Boa tarde';
      return 'Boa noite';
    }
  },
  mounted() {
    this.carregarDadosUsuario();
    this.carregarEstatisticas();
  },
  methods: {
    carregarDadosUsuario() {
      const userData = localStorage.getItem('auth_user');
      if (userData) {
        const { user } = JSON.parse(userData);
        this.userName = user?.name || 'Usuário';
        this.userOab = user?.oab || '';
      }
    },
    async carregarEstatisticas() {
      try {
        const [processosRes, clientesRes] = await Promise.allSettled([
          api.processos.getAll(),
          api.clientes.getAll()
        ]);

        if (processosRes.status === 'fulfilled') {
          const processos = processosRes.value.data;
          this.totalProcessos = processos.length;
          this.processosAtivos = processos.filter(p =>
            p.status && p.status.toLowerCase().includes('ativo')
          ).length;
        }

        if (clientesRes.status === 'fulfilled') {
          this.totalClientes = clientesRes.value.data.length;
        }
      } catch (e) {
        console.error("Erro ao carregar estatísticas", e);
      }
    }
  }
}
</script>

<style scoped>
.stat-card {
  background: white;
  padding: 24px;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}
</style>