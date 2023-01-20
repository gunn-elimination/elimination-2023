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

<script lang="ts">
import {EliminationUser} from '~/utils/types';

const leaderboard: {users: EliminationUser[]} = await (await fetch('https://xz.ax/game/scoreboard')).json();

export default {
    name: "Leaderboard",
    data() {
        return {
            leaderboard
        }
    }
}
</script>
