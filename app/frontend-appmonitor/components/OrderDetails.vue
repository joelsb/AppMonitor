<template>
    <div class="p-5 bg-gray-100 rounded-lg">
        <h3 class="text-xl font-semibold mb-4">Order Details</h3>
        <p><strong>Order ID:</strong> {{ order.id }}</p>
        <p><strong>Customer Name:</strong> {{ order.customerUsername }}</p>
        <p><strong>Order Date:</strong> {{ order.createdDate }}</p>
        <p><strong>Status:</strong> {{ order.deliveredDate ? 'Entregue' : 'Por entregar' }}</p>
        <ul>
            <li v-for="(volume, index) in order.volumes" :key="index">
                <p><strong>Volume {{ index + 1 }}:</strong></p>
                <p class="ml-4">ID: {{ volume.id }}</p>
                <p class="ml-4">SentDate: {{ volume.sentDate }}</p>
                <p class="ml-4">Status: {{ volume.deliveredDate ? 'Entregue' : 'Por entregar' }} </p>
            </li>
        </ul>
        <button @click="$emit('close')" class="mt-4 px-4 py-2 bg-red-500 text-white rounded">Close</button>
    </div>
</template>

<script>
export default {
    props: {
        order: {
            type: Object,
            required: true
        }
    }
};
</script>
