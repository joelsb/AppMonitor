<template>
    <NavBar/>
    <!-- Question inside a Card -->
    <div class="max-w-md mx-auto mt-6 p-5 bg-white rounded-lg shadow-md flex flex-col items-center">
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

// PopUp state
const showPopup = ref(false);
const popupTitle = ref('');
const popupMessage = ref('');
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

onMounted(() => {
    fetchData('/package-types', packageTypes);
    fetchData('/product-types', products);
    fetchData('/sensor-types', sensorTypes);
});

// Handle form submission
const handleFormSubmission = (status, message) => {
    if (status === 'success') {
        popupTitle.value = 'Success!';
        popupMessage.value = message;
        popupType.value = 'success';
    } if (status === 'error') {
        popupTitle.value = 'Error!';
        popupMessage.value = message;
        popupType.value = 'failure';
    }
    showPopup.value = true;
};

const handleInfoMandatorySensors = (sensors) => {
    console.log(sensors);
    popupTitle.value = 'Information!';

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

    let message = '';
    if (sensors.length === 0) {
        message = 'No mandatory sensors for this order';
    } else {
        // Construct the message with CSS inline and bullet points
        message = `
            <style>
                ul {
                    list-style-type: disc;
                    margin-left: 25px;
                }
                li {
                    margin-bottom: 7px;
                    margin-top: 7px;
                }
            </style>
            The following sensors are mandatory for this order:<br><ul>`;

        for (const [sensorName, count] of Object.entries(sensorCount)) {
            message += `<li>${count}x ${sensorName}</li>`;
        }

        message += '</ul>';
    }

    // Set the message and show the popup
    popupMessage.value = message;
    console.log(popupMessage.value);
    popupType.value = 'info';
    showPopup.value = true;
};

const handleInfoMandatoryPackage = (needsPackage) => {
    popupTitle.value = 'Information!';
    if (needsPackage) {
        popupMessage.value = `A mandatory package is <strong>required</strong> for this order.`;
    } else {
        popupMessage.value = `A mandatory package is <strong>not required</strong> for this order.`;
    }
    popupType.value = 'info';
    showPopup.value = true;
};



// Close the popup
const closePopup = () => {
    showPopup.value = false;
};
</script>

<style scoped>
/* Centered popup styles */
.fixed {
    display: flex;
    justify-content: center;
    align-items: center;
}

.bg-opacity-50 {
    backdrop-filter: blur(4px);
}
</style>
