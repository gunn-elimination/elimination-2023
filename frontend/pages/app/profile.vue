<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Profile</h1>
        <div class="px-8 py-12 bg-secondary text-secondary-content">
            <div class="sm:container flex gap-6 sm:gap-8">
                <Avatar
                    :user="store.currentUser"
                    large
                />

                <div class="flex flex-col">
                    <h2 class="font-bold text-2xl">
                        {{ store.currentUser.forename }} {{ store.currentUser.surname }}
                    </h2>
                    <span class="mb-2">{{ store.currentUser.email }}</span>

                    <span><strong>Eliminated:</strong> {{ !!store.currentUser.eliminatedBy }}</span>
                    <span v-if="store.currentUser.eliminatedBy">
                        <strong>Eliminated by:</strong>
                        <!-- TODO: fancier display -->
                        {{ store.currentUser.eliminatedBy.forename }} {{ store.currentUser.eliminatedBy.surname }}
                    </span>
                    <span><strong>EER (elimination-eliminated-ratio):</strong> {{ store.currentUser.eliminated.length }}.0</span>
                </div>
            </div>
        </div>
        <div class="p-8 sm:px-12">
            <h3 class="text-lg font-semibold text-secondary">Your eliminations:</h3>
            <p class="mt-6 sm:mt-12 text-secondary text-center italic" v-if="store.currentUser.eliminated.length === 0">
                You have not eliminated anyone yet.
            </p>
            <div v-else>
                <!-- TODO -->
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/user';

const store = useUserStore();
</script>
