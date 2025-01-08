<template>
    <div class="p-8">
        <form @submit.prevent="handleSubmit">
            <!-- Order ID -->
            <div class="mb-4">
                <label for="orderId" class="block font-semibold">Order ID:</label>
                <input id="orderId" v-model="form.id" type="number" min="0" max="1000"
                    class="w-full p-2 border border-gray-300 rounded" />
            </div>

            <!-- Created Date, only show if Order ID is valid -->
            <div class="mb-4" v-if="isOrderIdValid">
                <label for="createdDate" class="block font-semibold">Created Date:</label>
                <input id="createdDate" v-model="form.createdDate" type="datetime-local"
                    class="w-full p-2 border border-gray-300 rounded" />
            </div>

            <!-- Customer Username, only show if Created Date is valid -->
            <div class="mb-4" v-if="isCreatedDateValid">
                <label for="customerUsername" class="block font-semibold">Customer Username:</label>
                <select id="customerUsername" v-model="form.customerUsername"
                    class="w-full p-2 border border-gray-300 rounded">
                    <option v-for="customer in customers" :key="customer.id" :value="customer.username">{{
                        customer.username }}</option>
                </select>
            </div>

            <!-- Volume Section, only show if Customer Username is valid -->
            <div v-if="isCustomerUsernameValid">
                <div class="mb-4">
                    <label for="volumeId" class="block font-semibold">Volume ID:</label>
                    <input id="volumeId" v-model="form.volume.id" type="number" min="1001" max="9999"
                        class="w-full p-2 border border-gray-300 rounded" />
                </div>

                <!-- Sent Date, only show if Volume ID is valid -->
                <div class="mb-4" v-if="isVolumeIdValid">
                    <label for="sentDate" class="block font-semibold">Sent Date:</label>
                    <input id="sentDate" v-model="form.volume.sentDate" type="datetime-local"
                        class="w-full p-2 border border-gray-300 rounded" />
                </div>

                <!-- Products, only show if Sent Date is valid -->
                <fieldset class="mb-4 border border-gray-300 p-4 rounded" v-if="isSentDateValid">
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

                <!-- Package Type, only show if Products are valid -->
                <div class="mb-2" v-if="areProductsValid">
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

                <!-- Sensors Section, only show if Package Type is valid -->
                <fieldset class="border border-gray-300 p-4 rounded" v-if="isPackageTypeValid">
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
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, computed,  watch, onMounted } from 'vue';
import { useRuntimeConfig } from '#imports';

const config = useRuntimeConfig();
const apiUrl = config.public.API_URL;
const emit = defineEmits(['formSubmitted', 'infoMandatorySensors', 'infoMandatoryPackage']);
const orders = ref([]);
const volumes = ref([]);

const fetchOrders = async () => {
    orders.value = await fetch(`${apiUrl}/orders`).then(r => r.ok ? r.json() : []);
};
const fetchVolumes = async () => {
    volumes.value = await fetch(`${apiUrl}/volumes`).then(r => r.ok ? r.json() : []);
};

onMounted(() => {
    fetchOrders();
    fetchVolumes();
});

const { customers, packageTypes, products, sensorTypes, isOrderCreated } = defineProps({
    customers: { type: Array, required: true },
    packageTypes: { type: Array, required: true },
    products: { type: Array, required: true },
    sensorTypes: { type: Array, required: true },
    isOrderCreated: { type: Boolean, required: true },
});

const form = ref({
    id: 1,  // Default Order ID
    customerUsername: 'Tiago',  // Default customer username
    createdDate: '2024-12-21T10:00',  // Default created date (ISO format)
    volume: {
        orderId: 1,  // Default order reference
        id: 1001,  // Default volume ID
        sentDate: '2024-12-21T12:00',  // Default sent date (ISO format)
        packageTypeId: '',  // Default package type ID (empty, meaning no package selected)
        products: [
            { productId: 1, quantity: 1 },  // Default product with quantity
        ],
        sensors: [
            { id: null, sensorTypeId: null },  // Default sensor with sensor type
        ],
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

watch(() => [form.value.volume.products, form.value.volume.packageTypeId], () => fetchMandatorySensors(form.value.volume.products.map(p => p.productId), form.value.volume.packageTypeId), { deep: true });

const showMandatorySensors = () => emit('infoMandatorySensors', mandatorySensors.value);
const showMandatoryPackage = () => emit('infoMandatoryPackage', requiresMandatoryPackage.value);

const handleSubmit = async () => {
    if (!isOrderCreated.value) {
        // Create the order first
        const response = await fetch(`${apiUrl}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(form.value),
        });
        const data = await response.json();
        if (response.ok) {
            isOrderCreated.value = true;
        }
        emit('formSubmitted', response.ok ? 'success' : 'error', response.ok ? 'Order created successfully!' : data);
    } else {
        // Create the volume associated with the order
        form.value.volume.orderId = form.value.id;
        const response = await fetch(`${apiUrl}/volumes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(form.value.volume),
        });
        const data = await response.json();
        emit('formSubmitted', response.ok ? 'success' : 'error', response.ok ? 'Volume created successfully!' : data);
    }
};

const isOrderIdValid = computed(() => typeof form.value.id === 'number' && !orders.value.some(order => order.id === form.value.id) && form.value.id > 0);
const isCreatedDateValid = computed(() => form.value.createdDate !== '' && isOrderIdValid.value);
const isCustomerUsernameValid = computed(() =>
    //check if the username is equal to one of the usernames at the customers array
    customers.some(customer => customer.username === form.value.customerUsername) &&
    form.value.customerUsername !== '' && isCreatedDateValid.value);

const isVolumeIdValid = computed(() => {
    const volumeId = form.value.volume.id;
    return volumeId &&
        !volumes.value.some(volume => volume.id === volumeId) &&
        typeof volumeId === 'number' &&
        volumeId > 1000 && volumeId < 9999 &&
        isCustomerUsernameValid.value;
});
const isSentDateValid = computed(() => form.value.volume.sentDate !== '' && isVolumeIdValid.value);
const areProductsValid = computed(() => form.value.volume.products.every(product => product.productId && product.quantity > 0) && isSentDateValid.value);
const isPackageTypeValid = computed(() => {
    // Check if we require a mandatory package and whether products are valid
    return (!requiresMandatoryPackage.value || form.value.volume.packageTypeId) && areProductsValid.value;
});
const areSensorsValid = computed(() => {
    //TODO: Implement sensor validation
    if (mandatorySensors.value.length === 0) {
        return true;  // If there are no mandatory sensors, it's automatically valid
    }
    // Check if all mandatory sensors' id are in the form's sensors array with matching sensorTypeId
    return mandatorySensors.value.every(sensor => 
        form.value.volume.sensors.some(s => s.sensorTypeId === sensor.id)
    );
});

watch(() => form.value.volume.packageTypeId, () => {
    console.log('Mandatory Sensors:', mandatorySensors.value);
    console.log('Form Sensors:', form.value.volume.sensors);
}, { deep: true });
</script>
