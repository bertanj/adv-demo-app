import axios from 'axios';

const apiInstance = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// adiciona JWT em tudo q requer autenticação
apiInstance.interceptors.request.use((config) => {
  const userData = localStorage.getItem('auth_user');
  if (userData) {
    const { token } = JSON.parse(userData);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

// Generic CRUD factory
const createCrudEndpoints = (baseRoute) => ({
  getAll: () => apiInstance.get(`/${baseRoute}`),
  getById: (id) => apiInstance.get(`/${baseRoute}/${String(id)}`),
  create: (data) => apiInstance.post(`/${baseRoute}`, data),
  update: (id, data) => apiInstance.put(`/${baseRoute}/${String(id)}`, data),
  delete: (id) => apiInstance.delete(`/${baseRoute}/${String(id)}`)
});

export const api = {
  instance: apiInstance,
  advogados: {
    getAll: () => apiInstance.get('/advogados'),
    create: (data) => apiInstance.post('/advogados', data),
    createAsAdmin: (data) => apiInstance.post('/advogados/admin', data),
    update: (id, data) => apiInstance.put(`/advogados/${String(id)}`, data),
    delete: (id) => apiInstance.delete(`/advogados/${String(id)}`)
  },
  assistentes: createCrudEndpoints('assistentes'),
  clientes: createCrudEndpoints('clientes'),
  processos: createCrudEndpoints('processos'),
  usuarios: {
    create: (data) => apiInstance.post('/usuarios', data)
  }
};

export default api;
