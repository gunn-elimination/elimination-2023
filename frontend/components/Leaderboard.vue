<template>
    <div v-if="leaderboardStore.error" class="flex flex-col">
        <p class="mb-3">An error occurred fetching the leaderboard. Check the console for more info.</p>
        <code class="rounded-lg p-5 bg-base-200 font-mono text-secondary whitespace-pre-wrap">
            {{ leaderboardStore.error.type }} {{ leaderboardStore.error }}
        </code>
    </div>
    <div v-else-if="!leaderboardStore.leaderboard" class="flex gap-3 items-center text-secondary text-lg">
        <Spinner /> Loading leaderboard...
    </div>
    <ol v-else class="flex flex-col gap-2 list-decimal list-inside">
        <p v-if="leaderboardStore.participants" class="text-secondary italic mb-4">
            There are {{ leaderboardStore.participants }} users remaining.
        </p>
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
