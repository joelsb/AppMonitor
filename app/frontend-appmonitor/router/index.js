import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  
  // Outras rotas...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),  // Usa o histórico do navegador
  routes
});

export default router;
