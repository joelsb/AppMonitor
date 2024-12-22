<template>
    <div>
        <!-- NavBar (if necessary) -->
        <NavBar />

        <!-- Card to choose delivery type (Volume or Order) -->
        <div class="max-w-md mx-auto mt-12 mb-10 p-5 bg-white rounded-lg shadow-md flex flex-col items-center">
            <label for="deliveryType" class="block font-semibold text-lg">What do you want to deliver?</label>
            <div class="mt-4 flex items-center space-x-6">
                <!-- Radio Button for 'Order' -->
                <div class="flex items-center">
                    <input type="radio" id="order" value="Order" v-model="deliveryType" class="w-5 h-5 border-2 border-gray-600 rounded-full"
                        :checked="deliveryType === 'Order'" />
                    <label for="order" class="ml-2 text-lg">Order</label>
                </div>

                <!-- Radio Button for 'Volume' -->
                <div class="flex items-center">
                    <input type="radio" id="volume" value="Volume" v-model="deliveryType" class="w-5 h-5 border-2 border-gray-600 rounded-full"
                        :checked="deliveryType === 'Volume'" />
                    <label for="volume" class="ml-2 text-lg">Volume</label>
                </div>
            </div>

            <!-- Display the selected delivery type -->
            <div class="mt-4 text-lg">
                <p>You have selected to deliver: <strong>{{ deliveryType }}</strong></p>
            </div>
        </div>
    </div>

    <!-- Conditionally render Deliver Component passing order or volume as props -->
    <Deliver :deliveryType="deliveryType" :orders="orders" :volumes="volumes" />

</template>



<script setup>
import { ref, watch } from 'vue';
import { useRuntimeConfig } from '#imports';
import NavBar from '@/components/NavBar.vue'; // Import the NavBar component
import Deliver from '@/components/Deliver.vue';

// API and Router Setup
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const orders = ref([]);
const volumes = ref([]);

const deliveryType = ref('Order'); // Default is 'Order', can be changed to 'Volume'

// Fetch data based on the selected delivery type
const fetchDeliveryData = async (type) => {
    try {
        const response = await fetch(`${apiUrl}/${type.toLowerCase()}s`);
        const data = await response.json();
        if (type === 'Order') {
            orders.value = data;
        } else if (type === 'Volume') {
            volumes.value = data;
        }
    } catch (error) {
        console.error(`Error fetching ${type} data:`, error);
    }
};

onMounted(() => {
    fetchDeliveryData(deliveryType.value);
});

// Watch for changes in the delivery type and fetch new data
watch(deliveryType, async (newValue) => {
    // Wait for data to be fetched before logging
    await fetchDeliveryData(newValue);  
    
    console.log('Orders: ', orders.value);  
    console.log('Volumes: ', volumes.value);  
});
</script>
