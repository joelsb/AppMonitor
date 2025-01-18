<template>
    <div class="card">
        <div class="card-body">
            <h2 class="text-2xl font-bold text-gray-800 text-center mb-4">📡 Add Sensor Value</h2>

            <form class="form" @submit.prevent="addSensorValue">
                <!-- Sensor Selection -->
                <div class="mb-4">
                    <label for="sensorId" class="block font-semibold text-gray-700 pb-2">🔎 Select a Sensor:</label>
                    <select id="sensorId" v-model="form.sensorId" class="form-control">
                        <option value="" disabled>Select a Sensor</option>
                        <option v-for="sensor in sensors" :key="sensor.id" :value="sensor.id">
                            {{ sensor.id }} - {{ sensor.sensorType.name }}
                        </option>
                    </select>
                </div>

                <!-- Value Input -->
                <div class="form-group">
                    <label for="value" class="block font-semibold text-gray-700 pb-2">📊 Enter Value:</label>
                    <div class="input-group">
                        <input type="number" id="value" v-model="form.value" class="form-control enter-value"
                            placeholder="Enter Value" required />
                        <span class="unit-label">{{ unit }}</span>
                    </div>
                </div>

                <!-- Messages -->
                <div v-if="messages.length" class="form-messages">
                    <ul>
                        <li v-for="(message, index) in messages" :key="index" class="message-item">
                            ⚠️ {{ message }}
                        </li>
                    </ul>
                </div>

                <!-- Add Value Button -->
                <div class="edit-button">
                    <button type="submit" class="btn-primary">➕ Add Value</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, toRef, defineProps, defineEmits } from 'vue';
import { useRuntimeConfig } from '#imports';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const sensors = ref([]);

const emit = defineEmits(['addValue']);

const form = ref({
    sensorId: '',
    value: '',
    timestamp: '',
});

const messages = ref([]);

const addSensorValue = () => {
    if (!form.value.sensorId || !form.value.value) {
        messages.value = ['All fields are required.'];
        return;
    }
    messages.value = []; // Clear messages on successful validation
    emit('addValue', form.value);
    form.value.sensorId ='' ;
    form.value.value ='';
};

onMounted(async () => {
    try {
        const response = await fetch(`${apiUrl}/sensors`);
        sensors.value = await response.json();
    } catch (error) {
        console.error("Error fetching sensors:", error);
    }
});

const unit = computed(() => {
    const sensor = sensors.value.find(sensor => sensor.id === form.value.sensorId);
    return sensor?.sensorType?.unit || ''; // Safely check sensorType before accessing unit
});
</script>

<style scoped>
.card {
    max-width: 500px;
    margin: 2rem auto;
    padding: 1.5rem;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
    border: 1px solid #ddd;
}

.card-body {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

.form {
    display: flex;
    flex-direction: column;
    gap: 1.2rem;
}

.form-control {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 8px;
    font-size: 1rem;
    transition: border-color 0.3s ease;
}


.form-control:focus {
    border-color: #4f46e5;
    outline: none;
}

.input-group {
    display: flex;
    align-items: center;
    gap: 25px;
}

.unit-label {
    color: white;
    padding-right: 10px;
    font-size: 1rem;
    font-weight: bold;
    color: #555;
}

.form-messages ul {
    color: red;
    list-style: none;
    padding: 0;
    margin-top: 1rem;
    font-weight: bold;
}

.edit-button {
    display: flex;
    justify-content: center;
}

.btn-primary {
    background-color: #4f46e5;
    color: white;
    padding: 10px 15px;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
}

.btn-primary:hover {
    background-color: #4338ca;
}
</style>
