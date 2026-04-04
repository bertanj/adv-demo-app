import { createRouter, createWebHistory } from 'vue-router'
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import DashboardView from './views/DashboardView.vue'
import ClientesView from './views/ClientesView.vue'
import ProcessosView from './views/ProcessosView.vue'
import CadastrarAdvogadoView from './views/CadastrarAdvogadoView.vue'

const routes = [
  {
    path: '/',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: DashboardView
  },
  {
    path: '/clientes',
    name: 'clientes',
    component: ClientesView
  },
  {
    path: '/processos',
    name: 'processos',
    component: ProcessosView
  },
  {
    path: '/cadastrar-advogado',
    name: 'cadastrar-advogado',
    component: CadastrarAdvogadoView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
