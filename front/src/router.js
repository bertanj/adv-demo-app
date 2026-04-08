import { createRouter, createWebHistory } from 'vue-router'
import LoginView from './views/LoginView.vue'
import RegisterView from './views/RegisterView.vue'
import DashboardView from './views/DashboardView.vue'
import ClientesView from './views/ClientesView.vue'
import ProcessosView from './views/ProcessosView.vue'
import CadastrarAdvogadoView from './views/CadastrarAdvogadoView.vue'
import AssistentesView from './views/AssistentesView.vue'

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
    component: DashboardView,
    meta: { requiresAuth: true }
  },
  {
    path: '/clientes',
    name: 'clientes',
    component: ClientesView,
    meta: { requiresAuth: true }
  },
  {
    path: '/processos',
    name: 'processos',
    component: ProcessosView,
    meta: { requiresAuth: true }
  },
  {
    path: '/assistentes',
    name: 'assistentes',
    component: AssistentesView,
    meta: { requiresAuth: true }
  },
  {
    path: '/cadastrar-advogado',
    name: 'cadastrar-advogado',
    component: CadastrarAdvogadoView,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const authData = localStorage.getItem('auth_user')
  if (to.meta.requiresAuth && !authData) {
    next('/')
  } else if (to.meta.requiresAdmin) {
    const { user } = JSON.parse(authData)
    if (user?.role !== 'ADMIN') {
      next('/dashboard')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
