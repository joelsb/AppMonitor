import { createRouter, createWebHistory } from 'vue-router';
import OrderDetails from '@/pages/OrderDetails.vue'; // Importando o componente

const routes = [
    {
        path: '/order/:id',  // Definindo o parâmetro `id` na URL
        name: 'OrderDetails', // Nome da rota
        component: 'OrderDetails',// Componente para renderizar os detalhes da ordem
    },
    // Outras rotas...
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
