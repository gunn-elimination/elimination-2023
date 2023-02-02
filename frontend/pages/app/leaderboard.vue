<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Leaderboard</h1>
        <div class="p-8 bg-secondary text-secondary-content" v-if="store.currentUser">
            <h2 class="font-bold text-xl flex flex-row gap-2">
                You
            </h2>
            <div class="text-center flex flex-col">
                <Avatar
                    class="mb-2 mx-auto"
                    :user="store.currentUser"
                    large
                />

                <span class="font-bold text-lg">{{ store.currentUser.forename }} {{ store.currentUser.surname }}</span>
                <span class="text-sm">
                    <!-- #4 • -->
                    {{ store.currentUser.eliminated.length }}
                    elimination<template v-if="store.currentUser.eliminated.length !== 1">s</template>
                </span>
            </div>
        </div>
        <div class="px-6 sm:px-12 mt-6 pb-12">
            <client-only>
                <Leaderboard />
            </client-only>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/user';

const store = useUserStore();
</script>
