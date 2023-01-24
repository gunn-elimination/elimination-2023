<template>
    <li class="flex gap-4 items-center">
        <Avatar :user="user" />
        <p class="font-medium">{{ user.forename }} {{ user.surname }}</p>
        <p class="text-secondary italic">{{ user.eliminated.length }} eliminations</p>
    </li>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '~/utils/types';

export default {
    name: "LeaderboardUser",
    props: {
        user: Object as PropType<EliminationUser>
    }
}
</script>
