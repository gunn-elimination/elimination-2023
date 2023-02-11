<template>
    <section class="p-8 bg-secondary text-secondary-content">
        <h2 class="font-bold text-xl flex flex-row gap-2 mb-3">
            <slot />
        </h2>
        <div class="text-center flex flex-col items-center">
            <Avatar :user="user" large />
            <span class="font-bold text-lg">{{ user.forename }} {{ user.surname }}</span>
            <span class="text-sm">
                <!-- #4 • -->{{ user.eliminatedCount }}
                elimination<template v-if="user.eliminatedCount !== 1">s</template>
            </span>
        </div>
    </section>
</template>

<script setup lang="ts">
import {PropType} from 'vue';
import {EliminationUser} from '@/utils/types';

defineProps({
    user: Object as PropType<EliminationUser>
})
</script>
