<template>
  <aside :class="['sidebar relative transition-all duration-300 ease-in-out', isOpen ? 'w-[280px]' : 'w-[80px]']">
    <div class="logo-section" :class="isOpen ? 'justify-start' : 'justify-center'">
      <span class="logo-text">{{ isOpen ? 'WISE' : 'W' }}</span>
    </div>

    <nav class="nav-menu">
      <router-link to="/dashboard" class="nav-link group" active-class="active" :class="!isOpen ? 'justify-center !px-0' : ''">
        <span class="material-symbols-outlined nav-icon group-hover:text-blue-400 group-hover:drop-shadow-md">dashboard</span>
        <span v-if="isOpen" class="nav-text group-hover:text-blue-400 transition-colors">Dashboard</span>
      </router-link>

      <router-link to="/processos" class="nav-link group" active-class="active" :class="!isOpen ? 'justify-center !px-0' : ''">
        <span class="material-symbols-outlined nav-icon group-hover:text-blue-400 group-hover:drop-shadow-md">folder_open</span>
        <span v-if="isOpen" class="nav-text group-hover:text-blue-400 transition-colors">Processos</span>
      </router-link>

      <router-link to="/clientes" class="nav-link group" active-class="active" :class="!isOpen ? 'justify-center !px-0' : ''">
        <span class="material-symbols-outlined nav-icon group-hover:text-blue-400 group-hover:drop-shadow-md">groups</span>
        <span v-if="isOpen" class="nav-text group-hover:text-blue-400 transition-colors">Clientes</span>
      </router-link>

      <router-link v-if="isAdmin" to="/cadastrar-advogado" class="nav-link group" active-class="active" :class="!isOpen ? 'justify-center !px-0' : ''">
        <span class="material-symbols-outlined nav-icon group-hover:text-blue-400 group-hover:drop-shadow-md">person_add</span>
        <span v-if="isOpen" class="nav-text group-hover:text-blue-400 transition-colors">Cadastrar Advogado</span>
      </router-link>
    </nav>

    <div class="user-footer" :class="isOpen ? 'flex-row' : 'flex-col justify-center text-center'">
      <div v-if="isOpen" class="flex-1">
        <p class="user-name text-sm text-slate-300">{{ userName }}</p>
        <p class="text-xs text-slate-500">{{ userRole }}</p>
      </div>
      <router-link to="/" @click="logout" class="logout-link group" :class="!isOpen ? 'justify-center' : ''">
        <span class="material-symbols-outlined logout-icon group-hover:text-orange-400">logout</span>
        <span v-if="isOpen" class="group-hover:text-orange-400 transition-colors">Sair</span>
      </router-link>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'TheSidebar',
  props: {
    isOpen: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      userName: '',
      userRole: '',
      isAdmin: false
    }
  },
  mounted() {
    this.carregarUsuario();
  },
  methods: {
    carregarUsuario() {
      const userData = localStorage.getItem('auth_user');
      if (userData) {
        const { user } = JSON.parse(userData);
        this.userName = user?.name || 'Usuário';
        this.userRole = user?.role || '';
        this.isAdmin = user?.role === 'ADMIN';
      }
    },
    logout() {
      localStorage.removeItem('auth_user');
    }
  }
}
</script>

<style scoped>
.sidebar {
  background-color: #0f172a;
  color: white;
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow-x: hidden;
  white-space: nowrap;
}
.logo-section { padding: 30px; display: flex; align-items: center; gap: 10px; }
.logo-text { font-size: 20px; font-weight: bold; color: white; }
.nav-menu { flex: 1; padding: 0 10px; }
.nav-link {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px 20px;
  color: #94a3b8;
  text-decoration: none;
  border-radius: 10px;
  margin-bottom: 5px;
  transition: all 0.3s;
}
.nav-link:hover { background: #1e293b; color: white; }
.nav-icon { font-size: 20px; transition: all 0.3s; }
.active { background: rgba(30, 58, 138, 0.3) !important; color: white !important; border-left: 4px solid #3b82f6; }
.user-footer { padding: 20px; border-top: 1px solid #1e293b; display: flex; align-items: center; gap: 12px; }
.logout-link { color: #ec5b13; text-decoration: none; font-size: 14px; font-weight: bold; display: flex; align-items: center; gap: 12px; transition: all 0.3s; }
.logout-icon { font-size: 20px; }
</style>
