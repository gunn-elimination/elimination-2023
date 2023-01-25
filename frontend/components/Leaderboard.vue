<template>
    <div v-if="pending" class="flex gap-3 items-center text-secondary text-lg">
        <Spinner /> Loading leaderboard...
    </div>
    <div v-else-if="error">
        <p class="mb-4">An error occurred fetching the leaderboard:</p>
        <code class="rounded-lg p-5 bg-base-200 font-mono text-secondary whitespace-pre-wrap">
            {{ error }}
        </code>
    </div>
    <ol v-else class="flex flex-col gap-2 list-decimal list-inside">
        <LeaderboardUser
            v-for="user in leaderboard.users"
            :user="user"
        />
    </ol>
</template>

<script setup lang="ts">
const config = useRuntimeConfig();
const {data: leaderboard, pending, error} = useFetch(`${config.public.apiUrl}/game/scoreboard`);
</script>
