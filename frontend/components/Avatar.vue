<template>
    <!-- TODO: backend pfps -->
    <div v-if="false" class="avatar my-auto flex-none" :class="large ? 'w-24 h-24' : 'w-10 h-10'">
        <div
            class="w-full rounded-full ring-primary ring-offset-base-100 ring-offset-2"
            :class={ring:ring}
        >
            <img :src="src" />
        </div>
    </div>
    <div v-else class="avatar placeholder flex-none" :class="large ? 'w-24 h-24' : 'w-10 h-10'">
        <div class="bg-base-200 text-neutral-content rounded-full w-full">
            <span class="text-secondary" :class="large ? 'text-3xl' : 'text-lg'">
                {{ user.forename.charAt(0) + user.surname.charAt(0) }}
            </span>
        </div>
    </div>
</template>

<script setup lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '@/utils/types';

defineProps({
    ring: Boolean,
    large: Boolean,
    user: Object as PropType<EliminationUser>
})
</script>
