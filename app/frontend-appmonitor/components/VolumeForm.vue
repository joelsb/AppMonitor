<template>
    <div class="p-8">
        <form @submit.prevent="handleSubmit">
            <!-- Order ID -->
            <div class="mb-4">
                <label for="orderId" class="block font-semibold">Order ID:</label>
                <input id="orderId" v-model="form.orderId" type="number" class="w-full p-2 border rounded" />
            </div>

            <!-- Volume ID -->
            <div class="mb-4">
                <label for="volumeId" class="block font-semibold">Volume ID:</label>
                <input id="volumeId" v-model="form.id" type="number" class="w-full p-2 border rounded" />
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
                    <input :id="'quantity-' + index" v-model="product.quantity" type="number" class="w-full p-2 border rounded" />
                    <button type="button" @click="removeItem(form.products, index)" class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 mt-2">Remove</button>
                </div>
                <button type="button" @click="addItem(form.products)" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">Add Product</button>
            </fieldset>

            <!-- Package Type -->
            <div class="mb-2">
                <label class="flex items-center font-semibold">
                    Package Type:
                    <button type="button" @click="showMandatoryPackage" class="ml-2 text-blue-500 hover:text-blue-700"><i class="fas fa-info-circle"></i> Info</button>
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
                    <button type="button" @click="showMandatorySensors" class="ml-2 text-blue-500 hover:text-blue-700"><i class="fas fa-info-circle"></i> Info</button>
                </legend>
                <div v-for="(sensor, index) in form.sensors" :key="index" class="mb-2">
                    <label :for="'sensorTypeId-' + index" class="block">Sensor Type:</label>
                    <select :id="'sensorTypeId-' + index" v-model="sensor.sensorTypeId" class="w-full p-2 border rounded">
                        <option v-for="type in sensorTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
                    </select>
                    <label :for="'sensorId-' + index" class="block">Sensor ID:</label>
                    <input :id="'sensorId-' + index" v-model="sensor.id" type="number" class="w-full p-2 border rounded" />
                    <button type="button" @click="removeItem(form.sensors, index)" class="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600 mt-2">Remove</button>
                </div>
                <button type="button" @click="addItem(form.sensors)" class="bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600">Add Sensor</button>
            </fieldset>

            <!-- Submit Button -->
            <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600">Create new volume</button>
        </form>
    </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRuntimeConfig } from '#imports';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const emit = defineEmits(['formSubmitted', 'infoMandatorySensors', 'infoMandatoryPackage']);

const { packageTypes, products, sensorTypes } = defineProps({
    packageTypes: Array, products: Array, sensorTypes: Array
});

const form = ref({
    orderId: 27, id: 105, sentDate: '2021-06-01T00:00:00', packageTypeId: 1,
    products: [{ productId: 1, quantity: 1 }, { productId: 2, quantity: 3 }],
    sensors: [{ id: 1, sensorTypeId: 1 }, { id: 2, sensorTypeId: 2 }],
});

const requiresMandatoryPackage = computed(() =>
    form.value.products.some(p => products.find(prod => prod.id === p.productId)?.mandatoryPackage)
);

const handleSubmit = async () => {
    try {
        const res = await fetch(`${apiUrl}/volumes`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form.value)
        });
        const data = res.ok ? await res.json() : await res.text();
        emit('formSubmitted', res.ok ? 'success' : 'error', data);
    } catch (err) {
        emit('formSubmitted', 'error', err.message);
    }
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


