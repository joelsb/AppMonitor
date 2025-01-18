<template>
    <div class="card">
        <div class="mb-4 flex-row justify-between flex items-center">
            <button @click="router.go(-1)"
                class="px-6 py-2 bg-blue-500 text-white rounded-full hover:bg-blue-600 transition">
                Voltar
            </button>
            <div v-if="!isCreating" class="flex justify-end items-center">
                <label for="editProfileSwitch" class="block font-semibold text-lg mr-3">Edit Mode</label>
                <label for="editProfileSwitch" class="inline-flex relative items-center cursor-pointer">
                    <input type="checkbox" id="editProfileSwitch" v-model="isEditing" class="sr-only" />
                    <div class="w-14 h-6 rounded-full transition-colors"
                        :class="isEditing ? 'bg-blue-500' : 'bg-gray-200'"></div>
                    <div class="absolute left-1 top-1 w-4 h-4 bg-white rounded-full transition-transform"
                        :class="isEditing ? 'transform translate-x-7' : ''"></div>
                </label>
                <span class="ml-4 text-lg">{{ isEditing ? 'Editing' : 'View' }}</span>
            </div>
        </div>

        <div class="card-body">
            <form class="form" @submit.prevent="handleSubmit">
                <!-- ID -->
                <div class="mb-4">
                    <label for="id" class="block text-gray-700">ID</label>
                    <input type="number" min="1" id="id" v-model="form.id" class="form-control" required
                        :disabled="!isCreating && isEditing" />
                </div>

                <!-- Name -->
                <div class="mb-4">
                    <label for="name" class="block text-gray-700">Name</label>
                    <input type="text" id="name" v-model="form.name" class="form-control" required
                        :disabled="!isEditing && !isCreating" />
                </div>

                <!-- Mandatory Package -->
                <div class="mb-4">
                    <label class="block text-gray-700">
                        <input type="checkbox" v-model="form.mandatoryPackage" class="form-checkbox"
                            :disabled="!isEditing && !isCreating" />
                        Mandatory Package
                    </label>
                </div>

                <!-- Mandatory Sensors -->
                <div class="mb-4" v-if="form.mandatorySensors && form.mandatorySensors.length > 0">
                    <label for="mandatorySensors" class="block text-gray-700">Mandatory Sensors</label>
                    <input type="text" id="mandatorySensors" v-model="newSensor" class="form-control"
                        @keyup.enter="addSensor" placeholder="Add sensor and press Enter"
                        :disabled="!isEditing && !isCreating" />
                    <ul class="sensor-list mt-3">
                        <li v-for="(sensor, index) in form.mandatorySensors" :key="index" class="sensor-item">
                            {{ sensor }}
                            <button type="button" class="bg-red-500 text-white py-1 px-2 rounded hover:bg-red-600"
                                @click="removeSensor(index)">Remove</button>
                        </li>
                    </ul>
                </div>

                <!-- Submit Button -->
                <!-- Create Button (Only in Create Mode) -->
                <!-- Save Button (Only in Edit Mode) -->
                <div v-if="isEditing && isCreating" class="edit-button">
                    <button type="submit" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">
                        Create new Product Type
                    </button>
                </div>
                <!-- Create Button (Only in Create Mode) -->
                <div v-if="isEditing && !isCreating" class="edit-button">
                    <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">
                        Save Changes
                    </button>
                </div>
            </form>
        </div>
    </div>
    <Popup :show="showPopup" :title="popupTitle" :messages="popupMessages" :type="popupType" @close="closePopup" />
</template>


<script setup>
// Keep the script logic unchanged
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useRuntimeConfig } from '#imports';

const route = useRoute();
// Define props
const props = defineProps({
    productTypeData: Object,
    create: Boolean,
    edit: Boolean,
});

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;

const showPopup = ref(false);
const popupTitle = ref('');
const popupMessages = ref([]);
const popupType = ref('info');

// Define emits
const emit = defineEmits(['createProductType']);

// Vue Router instance
const router = useRouter();

// Reactive form data
const form = ref({});


// Fetch Package Type Data (if editing)
const fetchProductType = async (id) => {
    try {
        const response = await fetch(`${apiUrl}/product-types/${id}`);
        if (!response.ok) {
            let messages = await response.text()
            popupTitle.value = 'Error';
            popupMessages.value.push(messages);
            popupType.value = 'failure';
            showPopup.value = true;
        }
        form.value = await response.json();

    } catch (err) {
        popupTitle.value = 'Error';
        popupMessages.value.push(err.message);
        popupType.value = 'failure';
        showPopup.value = true;
    }
};

// On component mount, check if editing or creating
onMounted(() => {
    if (route.params.id) {
        // Editing mode
        fetchProductType(route.params.id);
    } else {
        // Creating a new package type
        isCreating.value = true;
        form.value = { id: null, name: '', mandatoryPackage: false, mandatorySensors: [] };
    }
});


// Ensure `mandatorySensors` is always an array
if (!form.mandatorySensors) {
    form.mandatorySensors = [];
}

// New sensor input
const newSensor = ref('');
const isEditing = ref(props.edit);
const isCreating = ref(props.create);

// Add sensor to the list
const addSensor = () => {
    if (newSensor.value.trim()) {
        form.mandatorySensors.push(newSensor.value.trim());
        newSensor.value = '';
    }
};

// Remove sensor from the list
const removeSensor = (index) => {
    form.mandatorySensors.splice(index, 1);
};

// Create or Edit Package Type
const handleSubmit = async () => {
    try {
        const method = isEditing.value && isCreating.value ? 'POST' : 'PUT';
        const url = isEditing.value && isCreating.value
            ? `${apiUrl}/product-types`
            : `${apiUrl}/product-types/${form.value.id}`;

        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(form.value),
        });

        if (!response.ok) {
            let messages = await response.text()
            popupTitle.value = 'Error';
            popupMessages.value.push(messages);
            popupType.value = 'failure';
            showPopup.value = true;
        }
        else {
            popupTitle.value = 'Success';
            popupMessages.value.push('Product Type saved successfully');
            popupType.value = 'success';
            showPopup.value = true;
        }

    } catch (err) {
        popupTitle.value = 'Error';
        popupMessages.value.push(err.message);
        popupType.value = 'failure';
        showPopup.value = true;
    }
};

// Watch for prop changes and update form reactively
watch(
    () => props.productTypeData,
    (newData) => {
        console.log('Updating form data: ', newData);
        Object.assign(form, newData);
    },
    { deep: true }
);

// Close Popup
const closePopup = () => {
    showPopup.value = false;
    popupMessages.value = [];
    router.go(-1);
};
</script>

<style scoped>
.card {
    max-width: 600px;
    margin: 2rem auto;
    padding: 1.5rem;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.card-header {
    text-align: center;
}

.card-header h2 {
    font-size: 1.5rem;
    font-weight: bold;
    color: #333333;
}

.card-body {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.mb-4 {
    margin-bottom: 1rem;
}

.form-control {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 0.375rem;
    font-size: 1rem;
}

.form-checkbox {
    margin-right: 0.5rem;
    transform: scale(1.2);
    cursor: pointer;
}

.sensor-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.5rem 1rem;
    border: 1px solid #e0e0e0;
    border-radius: 0.5rem;
    margin-bottom: 0.5rem;
    background-color: #f9f9f9;
}

.edit-button {
    display: flex;
    justify-content: flex-end;
}

.bg-green-500:hover {
    background-color: #45a049;
}

.bg-red-500:hover {
    background-color: #d32f2f;
}
</style>