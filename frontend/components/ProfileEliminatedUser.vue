<template>
    <div class="p-4 rounded-lg bg-base-200 flex gap-4">
        <Avatar :user="user" />
        <div class="flex flex-col">
            <p class="font-semibold">{{ user.forename }} {{ user.surname }}</p>
            <p class="text-sm text-secondary italic">
                {{ user.eliminations }}
                elimination<template v-if="user.eliminations !== 1">s</template>
            </p>
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '@/utils/types';

export default {
    name: "ProfileEliminatedUser",
    props: {
        user: Object as PropType<EliminationUser>
    }
}
</script>
