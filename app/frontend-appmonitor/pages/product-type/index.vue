<template>
    <NavBar />

    <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md gap-6 flex flex-col ">
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">Product Type Page</h2>
            <p class="mb-4 text-lg text-gray-600">See all product types.</p>
            <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600"
                @click="router.push('/product-type/show')">
                Product Types</button>
        </div>
        <div class="bg-white p-6 rounded-lg shadow-md">
            <h2 class="text-2xl font-semibold mb-4">New Product Type Page</h2>
            <p class="mb-4 text-lg text-gray-600">New product type page.</p>
            <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600"
                @click="router.push('/product-type/create')">
                New Product Type</button>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
</script>