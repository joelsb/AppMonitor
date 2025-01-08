

<template>
    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
        <!-- Header and Filters -->
        <div class="flex justify-between mb-4">
            <h1 class="text-3xl font-semibold">{{ deliveryType }}s</h1>
            <div class="flex items-center space-x-6">
                <div class="flex items-center">
                    <input type="radio" id="all" value="all" v-model="filter" class="w-5 h-5 border-2 border-gray-600 rounded-full">
                    <label for="all" class="ml-2 text-lg">All</label>
                </div>
                <div class="flex items-center">
                    <input type="radio" id="notDelivered" value="notDelivered" v-model="filter" class="w-5 h-5 border-2 border-gray-600 rounded-full">
                    <label for="notDelivered" class="ml-2 text-lg">{{ deliveryType }}s not Delivered</label>
                </div>
            </div>
        </div>

        <!-- Error Message -->
        <div v-if="errorMessage" class="mb-4 p-4 text-red-600 bg-red-100 border border-red-400 rounded">
            {{ errorMessage }}
        </div>

        <!-- Table -->
        <table class="min-w-full table-auto border-collapse mt-4">
            <thead>
                <tr>
                    <th class="px-4 py-2 text-left border-b w-1/12">ID</th>
                    <th class="px-4 py-2 text-left border-b w-2/12">{{ deliveryType === 'Volume' ? 'Order ID' : 'Customer Username' }}</th>
                    <th class="px-4 py-2 text-left border-b w-4/12">{{ deliveryType === 'Volume' ? 'Sent Date' : 'Created Date' }}</th>
                    <th class="px-4 py-2 text-left border-b w-4/12">Delivered Date</th>
                    <th class="px-4 py-2 text-left border-b w-2/12">Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in filteredItems" :key="index" class="hover:bg-gray-100">
                    <td class="px-4 py-2 border-b">{{ item.id }}</td>
                    <td class="px-4 py-2 border-b">{{ deliveryType === 'Volume' ? item.orderId : item.customerUsername }}</td>
                    <td class="px-4 py-2 border-b">{{ deliveryType === 'Volume' ? item.sentDate : item.createdDate }}</td>
                    <td class="px-4 py-2 border-b">{{ item.deliveredDate }}</td>
                    <td class="px-4 py-2 border-b">
                        <button class="bg-blue-500 text-white py-1 px-3 rounded" :class="{
                            'bg-blue-500 hover:bg-blue-600': !item.deliveredDate,
                            'bg-gray-400 cursor-not-allowed': item.deliveredDate
                        }" :disabled="item.deliveredDate" @click="deliver(item.id)">
                            Deliver
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
<script setup>
import { ref, computed, defineProps } from 'vue';
import axios from 'axios';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Props
const { volumes, orders, deliveryType } = defineProps({
    volumes: Array,
    orders: Array,
    deliveryType: String,
});

// Filter state
const filter = ref('all');  // "all" or "notDelivered"

// Error state
const errorMessage = ref(null);

// Filtered items computed property
const filteredItems = computed(() => {
    const items = deliveryType === 'Volume' ? volumes : orders;
    return items.filter(item => filter.value === 'all' || !item.deliveredDate);
});

// Deliver function
async function deliver(itemId) {
    const endpoint = deliveryType === 'Volume' ? 'volumes' : 'orders';
    const url = `${apiUrl}/${endpoint}/${itemId}/delivered`;

    try {
        const response = await axios.patch(url);
        console.info(`${deliveryType} entregue com sucesso:`, response.data);
        errorMessage.value = null; // Limpa a mensagem de erro, se houver
        return response.data;
    } catch (error) {
        const message = error.response?.data?.message || error.message || 'Erro desconhecido ao entregar.';
        console.error(`Erro ao entregar o ${deliveryType.toLowerCase()}:`, message);
        errorMessage.value = message; // Mostra a mensagem do console no erro na UI
        throw error;
    }
}
</script>