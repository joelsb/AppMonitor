<template>
    <div class="card">
        <div class="card-body">
            <form class="form" @submit.prevent="addSensorValue">
                <div class="mb-4">
                <label for="sensorId" class="block font-semibold">Sensor ID:</label>
                <select id="sensorId" v-model="form.sensorId" class="w-full p-2 border rounded">
                    <option value="" disabled>Select an Sensor</option>
                    <option v-for="sensor in sensors" :key="sensor.id" :value="sensor.id">
                        {{ sensor.id }}
                    </option>
                </select>
            </div>

                <!-- Value -->
                <div class="form-group">
                    <label for="value">Value</label>
                    <input 
                        type="number" 
                        id="value" 
                        v-model="form.value" 
                        class="form-control" 
                        placeholder="Enter Value" 
                        required 
                    />
                </div>


                <!-- Messages -->
                <div v-if="messages.length" class="form-messages">
                    <ul>
                        <li v-for="(message, index) in messages" :key="index" class="message-item">
                            {{ message }}
                        </li>
                    </ul>
                </div>

                <!-- Add Value Button -->
                <div class="edit-button">
                    <button type="submit" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">
                        Add Value
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { defineProps, defineEmits } from 'vue';
const { sensors } = defineProps({
   sensors: Array
});

const emit = defineEmits(['addValue']);

const form = ref({
    sensorId: '',
    value: '',
    timestamp: ''
});

const messages = ref([]);
const sensorIds = ref([]); // List of sensor IDs

const addSensorValue = () => {
    if (!form.value.sensorId || !form.value.value ) {
        messages.value = ['All fields are required.'];
        return;
    }
    messages.value = []; // Clear messages on successful validation
    emit('addValue', form.value);
};

</script>

<style scoped>
.form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.card-body {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.edit-button {
    display: flex;
    justify-content: flex-end;
}

.card {
    max-width: 600px;
    margin: 2rem auto;
    padding: 1.5rem;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.form-control {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 0.375rem;
    font-size: 1rem;
}

.form-messages ul {
    color: red;
    list-style: none;
    padding: 0;
    margin-top: 1rem;
}
</style>
