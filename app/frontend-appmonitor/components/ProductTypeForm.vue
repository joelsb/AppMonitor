<template>
    <div class="card">
        <div class="card-header">
            <h2>{{ create ? 'Create Product Type' : 'Edit Product Type' }}</h2>
        </div>
        <div class="card-body">
            <form class="form" @submit.prevent="onSubmit">
                <!-- ID -->
                <div class="form-group">
                    <label for="id" class="form-label">ID</label>
                    <input 
                        type="text" 
                        id="id" 
                        v-model="form.id" 
                        class="form-control" 
                        required 
                        :disabled="!create" 
                    />
                </div>

                <!-- Name -->
                <div class="form-group">
                    <label for="name" class="form-label">Name</label>
                    <input 
                        type="text" 
                        id="name" 
                        v-model="form.name" 
                        class="form-control" 
                        required 
                    />
                </div>

                <!-- Mandatory Package -->
                <div class="form-group">
                    <label class="form-label">
                        <input 
                            type="checkbox" 
                            v-model="form.mandatoryPackage" 
                            class="form-checkbox"
                        />
                        Mandatory Package
                    </label>
                </div>

                <!-- Mandatory Sensors -->
                <div class="form-group">
                    <label for="mandatorySensors" class="form-label">Mandatory Sensors</label>
                    <input 
                        type="text" 
                        id="mandatorySensors" 
                        v-model="newSensor" 
                        class="form-control" 
                        @keyup.enter="addSensor" 
                        placeholder="Add sensor and press Enter" 
                    />
                    <ul class="sensor-list">
                        <li 
                            v-for="(sensor, index) in form.mandatorySensors" 
                            :key="index" 
                            class="sensor-item"
                        >
                            {{ sensor }}
                            <button 
                                type="button" 
                                class="btn btn-secondary" 
                                @click="removeSensor(index)"
                            >
                                Remove
                            </button>
                        </li>
                    </ul>
                </div>

                <!-- Submit Button -->
                <div class="edit-button">
                    <button 
                        type="submit" 
                        class="btn btn-primary"
                    >
                        {{ create ? 'Create Product Type' : 'Save Changes' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        productTypeData: Object,
        create: Boolean,
    },
    emits: ['createProductType'],
    data() {
        return {
            form: { 
                ...this.productTypeData,
                mandatoryPackage: this.productTypeData.mandatoryPackage || false,
            },
            newSensor: '',
        };
    },
    methods: {
        addSensor() {
            if (this.newSensor.trim()) {
                this.form.mandatorySensors.push(this.newSensor.trim());
                this.newSensor = '';
            }
        },
        removeSensor(index) {
            this.form.mandatorySensors.splice(index, 1);
        },
        onSubmit() {
            this.$emit('createProductType', this.form);
        },
    },
};
</script>

<style scoped>
.card {
    max-width: 600px;
    margin: 2rem auto;
    padding: 2rem;
    border-radius: 0.75rem;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
}

.card-header {
    text-align: center;
    margin-bottom: 1.5rem;
}

.card-header h2 {
    font-size: 1.5rem;
    font-weight: bold;
    color: #333333;
}

.card-body {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

.form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.form-label {
    font-weight: 600;
    color: #4a4a4a;
}

.form-control {
    width: 100%;
    padding: 0.75rem 1rem;
    border: 1px solid #e0e0e0;
    border-radius: 0.5rem;
    font-size: 1rem;
    color: #333333;
}

.form-checkbox {
    margin-right: 0.5rem;
    transform: scale(1.2);
    cursor: pointer;
}

.form-control:disabled {
    background-color: #f8f8f8;
    color: #aaaaaa;
}

.sensor-list {
    list-style-type: none;
    padding: 0;
    margin: 0;
    margin-top: 0.5rem;
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

.btn {
    padding: 0.75rem 1.5rem;
    font-size: 1rem;
    font-weight: 600;
    border: none;
    border-radius: 0.5rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.btn-primary {
    background-color: #4caf50;
    color: #ffffff;
}

.btn-primary:hover {
    background-color: #45a049;
}

.btn-secondary {
    background-color: #f44336;
    color: #ffffff;
    padding: 0.5rem 1rem;
    font-size: 0.875rem;
    font-weight: 600;
    border-radius: 0.375rem;
    border: none;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.btn-secondary:hover {
    background-color: #d32f2f;
}
</style>
