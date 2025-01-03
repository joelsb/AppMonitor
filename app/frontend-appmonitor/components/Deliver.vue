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
import { ref, computed } from 'vue';

// Props
const { volumes, orders, deliveryType } = defineProps({
    volumes: Array,
    orders: Array,
    deliveryType: String,
});

// Filter state
const filter = ref('all');  // "all" or "notDelivered"

// Filtered items computed property
const filteredItems = computed(() => {
    const items = deliveryType === 'Volume' ? volumes : orders;
    return items.filter(item => filter.value === 'all' || !item.deliveredDate);
});

// Deliver function
const deliver = (item) => {
    item.deliveredDate = new Date().toISOString();
    // TODO: Implement API call to update delivery status
};
</script>

