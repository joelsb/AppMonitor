<template>
    <NavBar />
    <SensorForm 
        :sensorData="sensorForm" 
        :sensors="sensors"
        @addValue="addSensorValue"
    />

    <Popup 
        :show="showPopup" 
        :title="popupTitle" 
        :messages="popupMessages" 
        :type="popupType" 
        @close="closePopup" 
    />
</template>

<script setup>
import { useRuntimeConfig } from '#imports';
import { ref } from 'vue';
import NavBar from '~/components/NavBar.vue';
import SensorForm from '~/components/SensorForm.vue';
import Popup from '~/components/Popup.vue';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const sensors = ref([]);
const popupType = ref('info');

const fetchData = async (url, targetRef) => {
    try {
        const response = await fetch(`${apiUrl}${url}`);
        const data = await response.json();
        targetRef.value = data;
    } catch (error) {
        console.error(`Error fetching ${url}:`, error);
    }
};

const fetchSensors = async () => {
    fetchData('/sensors', sensors);
};


onMounted(() => {
    fetchSensors();
});

const sensorForm = ref({ 
    sensorId: '',
    value: ''
});

const addSensorValue = async (formValue) => {
    popupMessages.value = [];
    try {
        const response = await fetch(`${apiUrl}/sensors/${formValue.sensorId}/add-value`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                value: formValue.value,
            }),
        });

        const data = response.ok ? await response.json() : await response.text();
        if (!response.ok) {
            showPopup.value = true;
            popupTitle.value = 'Error';
            popupMessages.value.push(data || 'Failed to add value to the sensor.');
            popupType.value = 'failure';
        } else {
            
            showPopup.value = true;
            popupTitle.value = 'Success';
            popupMessages.value.push('Value added to the sensor successfully!');
            popupType.value = 'success';
        }
    } catch (error) {
        showPopup.value = true;
        popupTitle.value = 'Error';
        popupMessages.value.push('An error occurred while adding the value to the sensor.');
        popupType.value = 'error';
    }
};

const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
    sensorForm.value.sensorId = '';
    sensorForm.value.value = '';
};
</script>
