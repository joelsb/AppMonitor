<template>
    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
        <!-- Header and Filters -->
        <div class="flex justify-between mb-4">
            <h1 class="text-3xl font-semibold">{{ deliveryType }}s</h1>
            <div class="flex items-center space-x-6">
                <div class="flex items-center">
                    <input type="radio" id="all" value="all" v-model="filter"
                        class="w-5 h-5 border-2 border-gray-600 rounded-full">
                    <label for="all" class="ml-2 text-lg">All</label>
                </div>
                <div class="flex items-center">
                    <input type="radio" id="notDelivered" value="notDelivered" v-model="filter"
                        class="w-5 h-5 border-2 border-gray-600 rounded-full">
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
                    <th class="px-4 py-2 text-left border-b w-2/12">{{ deliveryType === 'Volume' ? 'Order ID' :
                        'Customer Username' }}</th>
                    <th class="px-4 py-2 text-left border-b w-4/12">{{ deliveryType === 'Volume' ? 'Sent Date' :
                        'Created Date' }}</th>
                    <th class="px-4 py-2 text-left border-b w-4/12">Delivered Date</th>
                    <th class="px-4 py-2 text-left border-b w-2/12">Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in filteredItems" :key="index" class="hover:bg-gray-100">
                    <td class="px-4 py-2 border-b">{{ item.id }}</td>
                    <td class="px-4 py-2 border-b">{{ deliveryType === 'Volume' ? item.orderId : item.customerUsername
                        }}</td>
                    <td class="px-4 py-2 border-b">{{ deliveryType === 'Volume' ? item.sentDate || 'Not Sent' :
                        item.createdDate || 'Not Created' }}</td>
                    <td class="px-4 py-2 border-b">{{ item.deliveredDate || 'Not Delivered' }}</td>
                    <td class="px-4 py-2 border-b">
                        <button class="bg-blue-500 text-white py-1 px-3 rounded" :class="{
                            'bg-blue-500 hover:bg-blue-600': !item.deliveredDate,
                            'bg-gray-400 cursor-not-allowed': item.deliveredDate
                        }" :disabled="item.deliveredDate" @click="deliver(item)">
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

    // Properly use map and return formatted items
    return items.map(item => {
        const formattedItem = { ...item };  // Clone the item to avoid mutating original data

        // Convert sentDate if available
        if (formattedItem.sentDate) {
            formattedItem.sentDate = new Date(formattedItem.sentDate).toLocaleString();
        }

        // Convert deliveredDate if available
        if (formattedItem.deliveredDate) {
            formattedItem.deliveredDate = new Date(formattedItem.deliveredDate).toLocaleString();
        }

        // Convert createdDate if available
        if (formattedItem.createdDate) {
            formattedItem.createdDate = new Date(formattedItem.createdDate).toLocaleString();
        }

        return formattedItem; // Return the formatted item
    }).filter(item => filter.value === 'all' || !item.deliveredDate);
});



// Deliver function
async function deliver(item) {
    const endpoint = deliveryType === 'Volume' ? 'volumes' : 'orders';

    try {
        const response = await fetch(`${apiUrl}/${endpoint}/${item.id}/deliver`, {
            method: 'PATCH', headers: { 'Content-Type': 'application/json' }
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
            emit('formSubmitted', 'success', `${deliveryType.slice(0, -1)} with id: \'${item.id}\' delivered successfully!`);

        } else {
            emit('formSubmitted', 'error', responseBody);
        }
    } catch (error) {
        emit('formSubmitted', 'error', error);
    }
}
</script>