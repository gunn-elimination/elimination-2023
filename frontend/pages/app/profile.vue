<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Profile</h1>
        <div class="px-8 py-12 bg-secondary text-secondary-content" v-if="store.currentUser">
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

                    <span><strong>Eliminated:</strong> {{ !!store.eliminatedBy }}</span>
                    <span v-if="store.eliminatedBy">
                        <strong>Eliminated by:</strong>
                        <!-- TODO: fancier display -->
                        {{ store.eliminatedBy.forename }} {{ store.eliminatedBy.surname }}
                    </span>
                    <span><strong>EER (elimination-eliminated-ratio):</strong> {{ store.currentUser.eliminated.length }}.0</span>
                </div>
            </div>
        </div>
        <div class="p-8 sm:px-12" v-if="store.currentUser">
            <h3 class="text-lg font-semibold text-secondary">Your eliminations:</h3>
            <p class="mt-6 sm:mt-12 text-secondary text-center italic" v-if="!store.currentUser.eliminated.length">
                You have not eliminated anyone yet.
            </p>
            <div v-else class="mt-6 flex flex-wrap gap-4 grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3">
                <ProfileEliminatedUser
                    v-for="user in store.currentUser.eliminated"
                    :user="user"
                />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/user';

const store = useUserStore();
</script>
