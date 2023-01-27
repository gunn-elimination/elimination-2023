<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Profile</h1>
        <div class="p-8 bg-secondary text-secondary-content">
            <div class="sm:container flex gap-6 sm:gap-8">
                <Avatar
                    :user="currentUser"
                    large
                />

                <div class="flex flex-col">
                    <h2 class="font-bold text-2xl">
                        {{ currentUser.forename }} {{ currentUser.surname }}
                    </h2>
                    <span class="mb-2">{{ currentUser.email }}</span>

                    <span><strong>Eliminated:</strong> {{ !!currentUser.eliminatedBy }}</span>
                    <span v-if="currentUser.eliminatedBy">
                        <strong>Eliminated by:</strong>
                        <!-- TODO: fancier display -->
                        {{ currentUser.eliminatedBy.forename }} {{ currentUser.eliminatedBy.surname }}
                    </span>
                    <span><strong>EER (elimination-eliminated-ratio):</strong> {{ currentUser.eliminated.length }}.0</span>
                </div>
            </div>
        </div>
        <div class="p-8">
            <h3 class="text-lg font-semibold text-secondary">Your eliminations:</h3>
            <p class="mt-6 text-secondary text-center italic" v-if="currentUser.eliminated.length === 0">
                You have not eliminated anyone yet.
            </p>
            <div v-else>
                <!-- TODO -->
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '@/utils/types';

export default {
    props: {
        currentUser: Object as PropType<EliminationUser>,
    }
};
</script>
