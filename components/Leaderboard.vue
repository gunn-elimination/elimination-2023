<template>
    <ol class="flex flex-col gap-2 list-decimal list-inside">
        <LeaderboardUser
            v-for="user in leaderboard.users"
            :forename="user.forename"
            :surname="user.surname"
            :eliminations="user.eliminated.length"
            :key="user.email"
        />
    </ol>
</template>

<script setup lang="ts">
const {data: leaderboard} = useFetch('https://xz.ax/game/scoreboard');
</script>
