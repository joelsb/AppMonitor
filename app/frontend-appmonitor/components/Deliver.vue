<template>
    <div class="max-w-4xl mx-auto mt-6 p-6 bg-white rounded-lg shadow-xl">
        <!-- Header and Filters -->
        <div class="flex justify-between mb-6">
            <h1 class="text-3xl font-semibold text-gray-800">{{ deliveryType }}s 🚚</h1>
            
        </div>

        <!-- Error Message -->
        <div v-if="errorMessage" class="mb-4 p-4 text-red-600 bg-red-100 border border-red-400 rounded">
            <strong>Error: </strong>{{ errorMessage }}
        </div>

        <div v-if="filteredItems.length === 0" class="text-center text-gray-500">
            No {{ deliveryType }}s to deliver 🙅‍♂️
        </div>

        <!-- Table -->
        <table v-if="filteredItems.length > 0" class="min-w-full table-auto border-collapse mt-6">
            <thead>
                <tr>
                    <th class="px-6 py-3 text-left border-b text-sm font-semibold text-gray-700">ID </th>
                    <th class="px-6 py-3 text-left border-b text-sm font-semibold text-gray-700">
                        {{ deliveryType === 'Volume' ? 'Order ID' : 'Customer Username' }}
                    </th>
                    <th class="px-6 py-3 text-left border-b text-sm font-semibold text-gray-700">
                        {{ deliveryType === 'Volume' ? 'Sent Date' : 'Created Date' }}
                    </th>
                    <th class="px-6 py-3 text-left border-b text-sm font-semibold text-gray-700">Delivered Date </th>
                    <th class="px-6 py-3 text-left border-b text-sm font-semibold text-gray-700">Actions </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in filteredItems" :key="index" class="hover:bg-gray-50 transition-all">
                    <td class="px-6 py-4 border-b text-sm text-gray-700">{{ item.id }}</td>
                    <td class="px-6 py-4 border-b text-sm text-gray-700">
                        {{ deliveryType === 'Volume' ? item.orderId : item.customerUsername }}
                    </td>
                    <td class="px-6 py-4 border-b text-sm text-gray-700">
                        {{ deliveryType === 'Volume' ? item.sentDate || 'Not Sent' : item.createdDate || 'Not Created' }}
                    </td>
                    <td class="px-6 py-4 border-b text-sm text-gray-700">
                        {{ item.deliveredDate || 'Not Delivered' }}
                    </td>
                    <td class="px-6 py-4 border-b text-sm text-gray-700">
                        <button
                            class="bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-blue-300"
                            :class="{
                                'bg-blue-500 hover:bg-blue-600': !item.deliveredDate,
                                'bg-gray-400 cursor-not-allowed': item.deliveredDate
                            }"
                            :disabled="item.deliveredDate"
                            @click="deliver(item)">
                            Deliver 📦
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { ref, computed, defineProps } from 'vue';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const emit = defineEmits(['formSubmitted']);

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
    return items
        .filter(item => filter.value === 'all' || !item.deliveredDate)
        .sort((a, b) => {
            // First sort by `orderId`, then by `id` within the same `orderId`
            if (a.orderId !== b.orderId) {
                return a.orderId - b.orderId;
            }
            return a.id - b.id;
        });
});

// Deliver function
async function deliver(item) {
    const endpoint = deliveryType === 'Volume' ? 'volumes' : 'orders';
    const messages = ref([]); // Initialize messages as an empty array
    try {
        const response = await fetch(`${apiUrl}/${endpoint}/${item.id}/deliver`, {
            method: 'PATCH'
        });

        const responseBody = await response.text();

        if (response.ok) {
            // Update the item locally to trigger reactivity
            const updatedItem = { ...item, deliveredDate: new Date().toLocaleString() };

            // Update the item in the correct list (volumes or orders)
            if (deliveryType === 'Volume') {
                const index = volumes.findIndex(v => v.id === item.id);
                if (index !== -1) {
                    volumes[index] = updatedItem;
                }
            } else {
                const index = orders.findIndex(o => o.id === item.id);
                if (index !== -1) {
                    orders[index] = updatedItem;
                }
            }

            // Add success message to the messages array
            messages.value.push(`${deliveryType.slice(0, -1)} with id: '${item.id}' delivered successfully! ✅`);
            emit('formSubmitted', 'success', messages.value);
        } else {
            // Add error response to the messages array
            messages.value.push(responseBody);
            emit('formSubmitted', 'error', messages.value);
        }
    } catch (error) {
        // Add error message to the messages array
        messages.value.push(`An error occurred: ${error.message} ❌`);
        emit('formSubmitted', 'error', messages.value);
    }
}
</script>

<style scoped>
/* Enhanced styles for better user experience */
.table-auto {
    border-collapse: collapse;
}

.table-auto th, .table-auto td {
    padding: 0.75rem;
}

.table-auto th {
    background-color: #f3f4f6;
}

button:disabled {
    opacity: 0.6;
}

button:hover:not(:disabled) {
    background-color: #3b82f6;
}

button:focus {
    outline: none;
}
</style>
