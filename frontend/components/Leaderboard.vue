<template>
    <div v-if="!leaderboardStore.leaderboard" class="flex gap-3 items-center text-secondary text-lg">
        <Spinner /> Loading leaderboard...
    </div>
    <div v-else-if="leaderboardStore.error">
        <p class="mb-4">An error occurred fetching the leaderboard:</p>
        <code class="rounded-lg p-5 bg-base-200 font-mono text-secondary whitespace-pre-wrap">
            {{ leaderboardStore.error.type }} {{ leaderboardStore.error }}
        </code>
    </div>
    <ol v-else class="flex flex-col gap-2 list-decimal list-inside">
        <LeaderboardUser
            v-for="user in leaderboardStore.leaderboard"
            :user="user"
        />
    </ol>
</template>

<script setup lang="ts">
import {useLeaderboardStore} from '@/store/leaderboard';

const leaderboardStore = useLeaderboardStore();
</script>
