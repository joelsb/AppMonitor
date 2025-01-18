<template>
    <NavBar/>
    <!-- Question inside a Card -->
    <div class="max-w-md mx-auto mt-12 mb-10 p-5 bg-white rounded-lg shadow-md flex flex-col items-center">
        <label for="orderCreated" class="block font-semibold text-lg">Is the order already created?</label>
        <div class="mt-4 flex items-center">
            <!-- Switch (Toggle) -->
            <label for="orderCreatedSwitch" class="inline-flex relative items-center cursor-pointer">
                <input type="checkbox" id="orderCreatedSwitch" v-model="isOrderCreated" class="sr-only" />
                <div class="w-14 h-6 rounded-full transition-colors"
                    :class="isOrderCreated ? 'bg-blue-500' : 'bg-gray-200'"></div> <!-- Dynamic background color -->
                <div class="absolute left-1 top-1 w-4 h-4 bg-white rounded-full transition-transform"
                    :class="isOrderCreated ? 'transform translate-x-7' : ''"></div> <!-- Adjusted for larger size -->
            </label>
            <span class="ml-4 text-lg">{{ isOrderCreated ? 'Yes' : 'No' }}</span>
        </div>
    </div>

    <!-- Conditionally render CombinedForm based on isOrderCreated
    <CombinedForm :customers="customers" :packageTypes="packageTypes" :products="products" :sensorTypes="sensorTypes" :isOrderCreated="isOrderCreated"
        @formSubmitted="handleFormSubmission" @infoMandatorySensors="handleInfoMandatorySensors"
        @infoMandatoryPackage="handleInfoMandatoryPackage" />
    {{ isOrderCreated }} -->

    <!-- Conditionally render OrderForm or VolumeForm based on isOrderCreated -->
    <div v-if="isOrderCreated === true">
        <VolumeForm :sensors = "sensors" :orders="orders" :volumes="volumes" :packageTypes="packageTypes" :products="products" :sensorTypes="sensorTypes"
            @formSubmitted="handleFormSubmission" @infoMandatorySensors="handleInfoMandatorySensors" @infoMandatoryPackage="handleInfoMandatoryPackage" />
    </div>

    <div v-if="isOrderCreated === false">
        <OrderForm :sensors = "sensors"  :customers="customers" :orders="orders" :volumes="volumes" :packageTypes="packageTypes" :products="products" :sensorTypes="sensorTypes"
            @formSubmitted="handleFormSubmission" @infoMandatorySensors="handleInfoMandatorySensors" @infoMandatoryPackage="handleInfoMandatoryPackage"/>
    </div>

    <!-- Success, Failure or Information Popup -->
    <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />

</template>


<script setup>
import { ref, onMounted } from 'vue';
import { useRuntimeConfig } from '#imports';

import NavBar from '@/components/NavBar.vue'; // Import the NavBar component
import VolumeForm from '@/components/VolumeForm.vue'; // Import the CombinedForm component
import OrderForm from '@/components/OrderForm.vue'; // Import the OrderForm component
import Popup from '@/components/Popup.vue'; // Import the Popup component

// API and Router Setup
const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

// Data arrays for selections
const customers = ref([]);
const packageTypes = ref([]);
const products = ref([]);
const sensorTypes = ref([]);
const orders = ref([]);
const volumes = ref([]);
const sensors = ref([]);

// PopUp state
const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info'); // Can be 'success' or 'error'
const isOrderCreated = ref(false);

// Fetch data function
const fetchData = async (url, targetRef) => {
    try {
        const response = await fetch(`${apiUrl}${url}`);
        const data = await response.json();
        targetRef.value = data;
    } catch (error) {
        console.error(`Error fetching ${url}:`, error);
    }
};

const fetchAll = async () => {
    fetchData('/customers', customers);
    fetchData('/volumes/available', volumes);
    fetchData('/orders/available', orders);
    fetchData('/package-types', packageTypes);
    fetchData('/product-types', products);
    fetchData('/sensor-types', sensorTypes);
    fetchData('/sensors', sensors);
};

onMounted(() => {
    fetchAll();
});

watch(isOrderCreated, (newValue) => {
    fetchAll();
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

const handleInfoMandatorySensors = (sensors) => {
    popupTitle.value = 'The following sensors are mandatory for this order:';

    // Create an object to hold the count of each sensor type
    const sensorCount = {};

    // Count occurrences of each sensor name
    sensors.forEach((sensor) => {
        if (sensorCount[sensor.name]) {
            sensorCount[sensor.name]++;
        } else {
            sensorCount[sensor.name] = 1;
        }
    });

    // Clear the existing messages array
    const messages = [];

    if (sensors.length === 0) {
        popupTitle.value = 'No mandatory sensors for this order';
    } else {
        // Add each sensor with its count to the messages array
        for (const [sensorName, count] of Object.entries(sensorCount)) {
            messages.push(`${count}x ${sensorName}`);
        }
    }

    // Set the messages array, popup type, and show the popup
    popupMessages.value = messages;
    popupType.value = 'info';
    showPopup.value = true;
};


const handleInfoMandatoryPackage = (needsPackage) => {
    popupTitle.value = 'Information!';
    popupMessages.value = [];
    if (needsPackage) {
        popupMessages.value.push(`A mandatory package is <strong>required</strong> for this order.`);
    } else {
        popupMessages.value.push(`A mandatory package is <strong>not required</strong> for this order.`);
    }
    popupType.value = 'info';
    showPopup.value = true;
};



// Close the popup
const closePopup = () => {
    showPopup.value = false;
};
</script>


