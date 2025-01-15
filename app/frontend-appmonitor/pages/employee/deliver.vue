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
                    <input type="radio" id="order" value="Order" v-model="deliveryType"
                        class="w-5 h-5 border-2 border-gray-600 rounded-full" :checked="deliveryType === 'Order'" />
                    <label for="order" class="ml-2 text-lg">Order</label>
                </div>

                <!-- Radio Button for 'Volume' -->
                <div class="flex items-center">
                    <input type="radio" id="volume" value="Volume" v-model="deliveryType"
                        class="w-5 h-5 border-2 border-gray-600 rounded-full" :checked="deliveryType === 'Volume'" />
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
    <Deliver :deliveryType="deliveryType" :orders="orders" :volumes="volumes" @formSubmitted="handleFormSubmission" />

    <!-- Success, Failure or Information Popup -->
    <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />


</template>



<script setup>
import { ref, watch } from 'vue';
import { useRuntimeConfig } from '#imports';
import { useAuthStore } from '~/store/auth-store';

import NavBar from '@/components/NavBar.vue'; // Import the NavBar component
import Deliver from '@/components/Deliver.vue';
import Popup from '@/components/Popup.vue'; // Import the Popup component

// API and Router Setup
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const orders = ref([]);
const volumes = ref([]);

// PopUp state
const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info'); // Can be 'success' or 'error'

const deliveryType = ref('Order'); // Default is 'Order', can be changed to 'Volume'

// Fetch data based on the selected delivery type
const fetchDeliveryData = async (type) => {
    try {
        //console.log(`Token data: ${token} \n User data: ${JSON.stringify(user)}`);
        const response = await fetch(`${apiUrl}/${type.toLowerCase()}s`, {
            method: 'GET'
        });

        if (response.status === 401) {
            console.error('Unauthorized access');
            // Handle the error, perhaps redirect to login or refresh token
            return;
        }
        if (response.ok) {
            const data = await response.json();
            if (type === 'Order') {
                orders.value = data;
            } else if (type === 'Volume') {
                volumes.value = data;
            }
        }
        else {
            popupTitle.value = 'Error!';
            popupMessages.value.push('The user is not authorized to access this data.');
            popupType.value = 'failure';
            showPopup.value = true;
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
});

// Handle form submission
const handleFormSubmission = (status, messages) => {
    if (status === 'success') {
        popupTitle.value = 'Success!';
        popupMessages.value = messages;
        popupType.value = 'success';
    } if (status === 'error') {
        popupTitle.value = 'Error!';
        popupMessages.value = messages;
        popupType.value = 'failure';
    }
    showPopup.value = true;
};

// Close the popup
const closePopup = () => {
    showPopup.value = false;
};
</script>
