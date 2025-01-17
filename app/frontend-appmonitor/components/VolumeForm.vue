<template>
    <div class="p-8">
        <form @submit.prevent="handleSubmit">
            <!-- Order ID -->
            <div class="mb-4">
                <label for="orderId" class="block font-semibold">Order ID:</label>
                <select id="orderId" v-model="form.orderId" class="w-full p-2 border rounded">
                    <option value="" disabled>Select an Order</option>
                    <option v-for="order in orders" :key="order.id" :value="order.id">
                        {{ order.id }}
                    </option>
                </select>
            </div>

            <!-- Volume ID -->
            <div class="mb-4">
                <label for="volumeId" class="block font-semibold">Volume ID:</label>
                <input id="volumeId" v-model="form.id" type="number" class="w-full p-2 border rounded"  />
            </div>

            <!-- Sent Date -->
            <div class="mb-4">
                <label for="sentDate" class="block font-semibold">Sent Date:</label>
                <input id="sentDate" v-model="form.sentDate" type="datetime-local" class="w-full p-2 border rounded" />
            </div>

            <!-- Products -->
            <fieldset class="mb-4 border p-4 rounded">
                <legend class="font-semibold">Products:</legend>
                <div v-for="(product, index) in form.products" :key="index" class="mb-2">
                    <label :for="'productId-' + index" class="block">Product:</label>
                    <select :id="'productId-' + index" v-model="product.productId" class="w-full p-2 border rounded">
                        <option v-for="productType in products" :key="productType.id" :value="productType.id">
                            {{ productType.name }}
                        </option>
                    </select>
                    <label :for="'quantity-' + index" class="block">Quantity:</label>
                    <input :id="'quantity-' + index" v-model="product.quantity" type="number"
                        class="w-full p-2 border rounded" />
                    <button type="button" @click="removeItem(form.products, index)"
                        class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 mt-2">Remove</button>
                </div>
                <button type="button" @click="addItem(form.products)"
                    class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">Add Product</button>
            </fieldset>

            <!-- Package Type -->
            <div class="mb-2">
                <label class="flex items-center font-semibold">
                    Package Type:
                    <button type="button" @click="showMandatoryPackage"
                        class="ml-2 text-blue-500 hover:text-blue-700"><i class="fas fa-info-circle"></i> Info</button>
                </label>
                <select v-model="form.packageTypeId" class="w-full p-2 border rounded">
                    <option value="">No Package Type</option>
                    <option v-for="type in packageTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
                </select>
                <p v-if="requiresMandatoryPackage && !form.packageTypeId" class="text-red-500 text-sm mt-1">
                    One or more products require a package. Please select a package type.
                </p>
            </div>

            <!-- Sensors Section -->
            <fieldset class="border p-4 rounded mb-4">
                <legend class="font-semibold flex items-center">
                    Sensors:
                    <button type="button" @click="showMandatorySensors"
                        class="ml-2 text-blue-500 hover:text-blue-700"><i class="fas fa-info-circle"></i> Info</button>
                </legend>
                <div v-for="(sensor, index) in form.sensors" :key="index" class="mb-2">
                    <label :for="'sensorTypeId-' + index" class="block">Sensor Type:</label>
                    <select :id="'sensorTypeId-' + index" v-model="sensor.sensorTypeId"
                        class="w-full p-2 border rounded">
                        <option v-for="type in sensorTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
                    </select>
                    <button type="button" @click="removeItem(form.sensors, index)"
                        class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 mt-2">Remove</button>
                </div>
                <button type="button" @click="addItem(form.sensors)"
                    class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">Add Sensor</button>
            </fieldset>

            <!-- Submit Button -->
            <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">Create new
                volume</button>
        </form>
    </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRuntimeConfig } from '#imports';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const emit = defineEmits(['formSubmitted', 'infoMandatorySensors', 'infoMandatoryPackage']);

const { orders, volumes,packageTypes, products, sensorTypes } = defineProps({
    orders: Array,volumes: Array, packageTypes: Array, products: Array, sensorTypes: Array
});
const nextVolumeId = computed(() => {
    // Get the highest volume ID from volumes
    const lastId = volumes.reduce((max, volume) => Math.max(max, volume.id), 0);
    return lastId + 1;
});
const nextSensorId = computed(() => {
    // Get the highest volume ID from orders
    const lastId = sensors.reduce((max, sensor) => Math.max(max, sensor.id), 0);
    return lastId + 1;
});


const form = ref({
    orderId: 28, id: nextVolumeId, sentDate: '2021-06-01T00:00:00', packageTypeId: 1,
    products: [{ productId: 1, quantity: 1 }, { productId: 2, quantity: 3 }],
    sensors: [{ id: nextSensorId, sensorTypeId: 1 }, { id: nextSensorId+1, sensorTypeId: 2 }],
});


const requiresMandatoryPackage = computed(() =>
    form.value.products.some(p => products.find(prod => prod.id === p.productId)?.mandatoryPackage)
);

const validatedForm = () => {
    const errors = [];

    // Check if the Order ID is provided
    if (!form.value.orderId) {
        errors.push('Order ID is required.');
    }

    // Check if the Volume ID is provided
    if (!form.value.id) {
        errors.push('Volume ID is required.');
    }

    // Check if the Sent Date is provided
    if (!form.value.sentDate) {
        errors.push('Sent Date is required.');
    }

    // Check if all products have valid product IDs and quantities
    const invalidProducts = form.value.products.find(product => !product.productId || !product.quantity);
    if (invalidProducts) {
        errors.push('All products must have a valid product type and quantity.');
    }

    // Check if all sensors have valid sensor type IDs and IDs
    const invalidSensors = form.value.sensors.find(sensor => !sensor.sensorTypeId || !sensor.id);
    if (invalidSensors) {
        errors.push('All sensors must have a valid sensor type and ID.');
    }

    // Check if a package type is selected when required
    if (requiresMandatoryPackage.value && !form.value.packageTypeId) {
        errors.push('One or more products require a package. Please select a package type.');
    }

    // Return the array of errors if any; otherwise, return false
    return errors.length > 0 ? errors : false;
};


const handleSubmit = async () => {
    const errors = validatedForm();
    if (errors) {
        emit('formSubmitted', 'error', errors);
        return;
    }
    const response = await fetch(`${apiUrl}/volumes`, {
        method: 'POST', body: JSON.stringify(form.value)
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

const addItem = (array) => array.push({ productId: null, quantity: null });
const removeItem = (array, index) => array.splice(index, 1);

const mandatorySensors = ref([]);

const fetchMandatorySensors = async (productTypeId, packageTypeId) => {
    const productSensors = await fetch(`${apiUrl}/product-types/${productTypeId}/mandatory-sensors`).then(r => r.ok ? r.json() : []);
    const packageSensors = packageTypeId ? await fetch(`${apiUrl}/package-types/${packageTypeId}/mandatory-sensors`).then(r => r.ok ? r.json() : []) : [];
    mandatorySensors.value = [...productSensors, ...packageSensors];
};

const showMandatorySensors = () => {
    // Call fetchMandatorySensors and use .then() to wait for it to resolve
    fetchMandatorySensors(form.value.products[0].productId, form.value.packageTypeId)
        .then(() => {
            // After fetch is complete, emit the event with the mandatorySensors value
            emit('infoMandatorySensors', mandatorySensors.value);
        })
        .catch((error) => {
            console.error('Error fetching mandatory sensors:', error);
        });
};

const showMandatoryPackage = () => emit('infoMandatoryPackage', requiresMandatoryPackage.value);
</script>
