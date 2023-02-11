<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Leaderboard</h1>
        <PlayerSection :user="store.currentUser" v-if="store.currentUser">
            You
        </PlayerSection>
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
