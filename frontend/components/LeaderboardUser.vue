<template>
    <li class="flex gap-4 items-center" :class="{'line-through': user.isEliminated}">
        <Avatar :user="user" />
        <p class="font-medium">{{ user.forename }} {{ user.surname }}</p>
        <p class="text-secondary italic">
            {{ user.eliminatedCount }}
            elimination<template v-if="user.eliminatedCount !== 1">s</template>
        </p>
    </li>
</template>

<script setup lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '@/utils/types';

defineProps({
    user: Object as PropType<EliminationUser>
})
</script>
