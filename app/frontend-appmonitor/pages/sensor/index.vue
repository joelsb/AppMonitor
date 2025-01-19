<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Card to choose delivery type (Volume or Order) -->
        

        <div class="max-w-4xl mx-auto mt-6 p-5 bg-white rounded-lg shadow-md">
            <div class="bg-white p-6 rounded-lg shadow-md mb-6">
                <h2 class="text-2xl font-semibold mb-4">Sensor Page</h2>
                <p class="mb-4 text-lg text-gray-600">See all the sensors and a detailed sensor</p>
                <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="router.push('/sensor/show')">
                    Sensors</button>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-2xl font-semibold mb-4">Add Value Page</h2>
                <p class="mb-4 text-lg text-gray-600">Add value to a sensor.</p>
                <button class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600" @click="router.push('/sensor/add-value')">
                    Add Value</button>
                </div>
            </div>
    </div>
</template>
<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

</script>