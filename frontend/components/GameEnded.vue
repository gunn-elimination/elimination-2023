<template>
    <header class="flex sticky-header flex-row gap-8 p-8">
        <div class="my-auto flex hidden sm:block">
            <Avatar
                ring
                :user="userStore.currentUser"
                v-if="userStore.currentUser"
            />
        </div>
        <h2 class="font-bold text-3xl text-primary w-full align-middle my-auto">
            The game has ended!
        </h2>
    </header>
    <!-- TODO: this logic assumes that the person with the most eliminations is not dead -->
    <!-- that is true for this year, but we should consider changing it for later years -->
    <PlayerSection :user="leaderboardStore.leaderboard[0]" v-if="leaderboardStore.leaderboard">
        This year's winner:
    </PlayerSection>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/user';
import {useLeaderboardStore} from '@/store/leaderboard';

const userStore = useUserStore();
const leaderboardStore = useLeaderboardStore();
</script>
