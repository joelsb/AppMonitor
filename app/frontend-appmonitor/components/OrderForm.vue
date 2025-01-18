<template>
    <div class="p-8">
        <form @submit.prevent="handleSubmit">
            <!-- Order ID -->
            <div class="mb-4">
                <label for="orderId" class="block font-semibold">Order ID:</label>
                <input id="orderId" v-model="form.id" type="number" class="w-full p-2 border border-gray-300 rounded"  />
            </div>

            <!-- Created Date -->
            <div class="mb-4">
                <label for="createdDate" class="block font-semibold">Created Date:</label>
                <input id="createdDate" v-model="form.createdDate" type="datetime-local"
                    class="w-full p-2 border border-gray-300 rounded" />
            </div>

            <!-- Customer Username -->
            <div class="mb-4">
                <label for="customerUsername" class="block font-semibold">Customer Username:</label>
                <select id="customerUsername" v-model="form.customerUsername"
                    class="w-full p-2 border border-gray-300 rounded">
                    <option v-for="customer in customers" :key="customer.username" :value="customer.username">{{
                        customer.username }}</option>
                </select>
            </div>

            <!-- Volume -->
            <fieldset class="mb-4 border border-gray-300 p-4 rounded">
                <legend class="font-semibold">Volume:</legend>

                <!-- Volume ID -->
                <div class="mb-2">
                    <label for="volumeId" class="block">Volume ID:</label>
                    <input id="volumeId" v-model="form.volume.id" type="number"
                        class="w-full p-2 border border-gray-300 rounded"   />
                </div>

                <!-- Sent Date -->
                <div class="mb-4">
                    <label for="sentDate" class="block">Sent Date:</label>
                    <input id="sentDate" v-model="form.volume.sentDate" type="datetime-local"
                        class="w-full p-2 border border-gray-300 rounded" />
                </div>

                <!-- Products -->
                <fieldset class="mb-4 border border-gray-300 p-4 rounded">
                    <legend class="font-semibold">Products:</legend>
                    <div v-for="(product, index) in form.volume.products" :key="index" class="mb-2">
                        <label :for="'productId-' + index" class="block">Product:</label>
                        <select :id="'productId-' + index" v-model="product.productId"
                            class="w-full p-2 border border-gray-300 rounded">
                            <option v-for="productType in products" :key="productType.id" :value="productType.id">{{
                                productType.name }}</option>
                        </select>
                        <label :for="'quantity-' + index" class="block">Quantity:</label>
                        <input :id="'quantity-' + index" v-model="product.quantity" type="number"
                            class="w-full p-2 border border-gray-300 rounded" />
                        <button type="button" @click="removeItem('product', index)"
                            class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 mt-2">Remove</button>
                    </div>
                    <button type="button" @click="addItem('product')"
                        class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">Add Product</button>
                </fieldset>

                <!-- Package Type -->
                <div class="mb-2">
                    <label class="flex items-center">
                        Package Type:
                        <button type="button" @click="showMandatoryPackage"
                            class="ml-2 text-blue-500 hover:text-blue-700">
                            <i class="fas fa-info-circle"></i> Info
                        </button>
                    </label>
                    <select id="packageTypeId" v-model="form.volume.packageTypeId"
                        class="w-full p-2 border border-gray-300 rounded">
                        <option value="">No Package Type</option>
                        <option v-for="type in packageTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
                    </select>
                    <p v-if="requiresMandatoryPackage && !form.volume.packageTypeId" class="text-red-500 text-sm mt-1">
                        One or more products require a package. Please select a package type.
                    </p>
                </div>

                <!-- Sensors -->
                <fieldset class="border border-gray-300 p-4 rounded">
                    <legend class="font-semibold flex items-center">
                        Sensors:
                        <button type="button" @click="showMandatorySensors"
                            class="ml-2 text-blue-500 hover:text-blue-700">
                            <i class="fas fa-info-circle"></i> Info
                        </button>
                    </legend>
                    <div v-for="(sensor, index) in form.volume.sensors" :key="index" class="mb-2">
                        <label :for="'sensorTypeId-' + index" class="block">Sensor Type:</label>
                        <select :id="'sensorTypeId-' + index" v-model="sensor.sensorTypeId"
                            class="w-full p-2 border border-gray-300 rounded">
                            <option v-for="type in sensorTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
                        </select>
                        <label :for="'sensorId-' + index" class="block">Sensor ID:</label>
                        <input :id="'sensorId-' + index" v-model="sensor.id" type="number"
                            class="w-full p-2 border border-gray-300 rounded" />
                        <button type="button" @click="removeItem('sensor', index)"
                            class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 mt-2">Remove</button>
                    </div>
                    <button type="button" @click="addItem('sensor')"
                        class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">Add Sensor</button>
                </fieldset>
            </fieldset>

            <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600"
                :disabled="requiresMandatoryPackage && !form.volume.packageTypeId">
                Create new order with volume
            </button>
        </form>
    </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRuntimeConfig } from '#imports';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const emit = defineEmits(['formSubmitted', 'infoMandatorySensors', 'infoMandatoryPackage']);

const { customers, packageTypes, sensors,products, sensorTypes,orders,volumes } = defineProps({
    customers: { type: Array, required: true },
    packageTypes: { type: Array, required: true },
    products: { type: Array, required: true },
    sensorTypes: { type: Array, required: true },
    sensors: { type: Array, required: true },
    orders: { type: Array, required: true },
    volumes: { type: Array, required: true },
});

const nextVolumeId = computed(() => {
    // Get the highest volume ID from volumes
    const lastId = volumes.reduce((max, volume) => Math.max(max, volume.id), 0);
    return lastId + 1;
});

const nextOrderId = computed(() => {
    // Get the highest volume ID from orders
    const lastId = orders.reduce((max, order) => Math.max(max, order.id), 0);
    return lastId + 1;
});

const nextSensorId = computed(() => {
    // Get the highest volume ID from orders
    const lastId = sensors.reduce((max, sensor) => Math.max(max, sensor.id), 0);
    return lastId + 1;
});


const form = ref({
    id: nextOrderId, customerUsername: 'Tiago', createdDate: '2024-11-20T21:00:00',
    volume: {
        id: nextVolumeId, sentDate: '2024-11-21 21:00:00', packageTypeId: 1,
        products: [{ productId: 2, quantity: 1 }], sensors: [{ id: nextSensorId, sensorTypeId: 2 }]
    },
});



const requiresMandatoryPackage = computed(() => form.value.volume.products.some(product => products.find(p => p.id === product.productId)?.mandatoryPackage));

const addItem = (type) => form.value.volume[type + 's'].push(type === 'product' ? { productId: null, quantity: null } : { id: null, sensorTypeId: null });
const removeItem = (type, index) => form.value.volume[type + 's'].splice(index, 1);
const mandatorySensors = ref([]);

const fetchMandatorySensors = async (productTypeId, packageTypeId) => {
    const productSensors = await fetch(`${apiUrl}/product-types/${productTypeId}/mandatory-sensors`).then(r => r.ok ? r.json() : []);
    const packageSensors = packageTypeId ? await fetch(`${apiUrl}/package-types/${packageTypeId}/mandatory-sensors`).then(r => r.ok ? r.json() : []) : [];
    mandatorySensors.value = [...productSensors, ...packageSensors];
};

const showMandatorySensors = () => {
    // Call fetchMandatorySensors and use .then() to wait for it to resolve
    fetchMandatorySensors(form.value.volume.products[0].productId, form.value.volume.packageTypeId)
        .then(() => {
            // After fetch is complete, emit the event with the mandatorySensors value
            emit('infoMandatorySensors', mandatorySensors.value);
        })
        .catch((error) => {
            console.error('Error fetching mandatory sensors:', error);
        });
};

const showMandatoryPackage = () => emit('infoMandatoryPackage', requiresMandatoryPackage.value);
const validatedForm = () => {
    const errors = [];

    // Check if the Order ID is provided
    if (!form.value.id) {
        errors.push('Order ID is required.');
    }

    // Check if the Order Created Date is provided and valid
    if (!form.value.createdDate) {
        errors.push('Order Created Date is required.');
    }

    // Check if the Customer Username is provided
    if (!form.value.customerUsername) {
        errors.push('Customer Username is required.');
    }

    // Check if the Volume ID is provided
    if (!form.value.volume.id) {
        errors.push('Volume ID is required.');
    }

    // Check if the Volume Sent Date is provided and valid
    if (!form.value.volume.sentDate) {
        errors.push('Volume Sent Date is required.');
    }

    // Check if the Volume's products have valid product IDs and quantities
    const invalidProducts = form.value.volume.products.find(product => !product.productId || !product.quantity);
    if (invalidProducts) {
        errors.push('All products must have a valid product type and quantity.');
    }

    // Check if the Volume's sensors have valid sensor type IDs and IDs
    const invalidSensors = form.value.volume.sensors.find(sensor => !sensor.sensorTypeId || !sensor.id);
    if (invalidSensors) {
        errors.push('All sensors must have a valid sensor type and ID.');
    }

    // Check if a package type is selected when required
    if (requiresMandatoryPackage.value && !form.value.volume.packageTypeId) {
        errors.push('One or more products require a package. Please select a package type.');
    }

    // If there are errors, return the array of errors; otherwise, return false
    return errors.length > 0 ? errors : false;
};

const formatDateToBackend = (dateString) => {
    if (!dateString) return null; // Retorna null se a data não estiver definida
    const date = new Date(dateString);
    const pad = (num) => num.toString().padStart(2, '0'); // Garante dois dígitos
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
};


const handleSubmit = async () => {
    const validationErrors = validatedForm();
    // Formatar datas para o backend
    form.value.createdDate = formatDateToBackend(form.value.createdDate);
    form.value.volume.sentDate = formatDateToBackend(form.value.volume.sentDate);

    if (validationErrors) {
        // Display all validation errors
        emit('formSubmitted', 'error', validationErrors);
        return;
    }

    const response = await fetch(`${apiUrl}/orders`, {
        method: 'POST', body: JSON.stringify(form.value),
    });

    const data = response.ok ? await response.json() : await response.text();
    const messages = ref([]);

    // Check if the data is a string or array
    if (typeof data === 'string') {
        // Add the single error message to the messages array
        messages.value.push(data);
    } else if (Array.isArray(data)) {
        // If the data is an array of messages, spread it into the messages array
        messages.value.push(...data);
    } else if (response.ok) {
        // Created successfully
        messages.value.push('Order created successfully!');
    }

    // Emit the form submission result
    emit('formSubmitted', response.ok ? 'success' : 'error', response.ok ? messages.value : messages.value);

};

</script>