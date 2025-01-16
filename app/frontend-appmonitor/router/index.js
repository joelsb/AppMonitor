import { createRouter, createWebHistory } from 'vue-router';
import OrderDetails from '@/components/OrderDetails.vue';

const routes = [
  {
    path: '/order/:id',
    name: 'OrderDetails',  // Certifique-se de que o nome da rota é 'OrderDetails'
    component: OrderDetails,
    props: true  // Passa o parâmetro id como uma prop para o componente
  }
  // Outras rotas...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),  // Usa o histórico do navegador
  routes
});

export default router;
