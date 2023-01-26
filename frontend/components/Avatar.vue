<template>
    <div v-if="false" class="avatar my-auto" :class="large ? 'w-24' : 'w-10'">
        <div
            class="w-full rounded-full ring-primary ring-offset-base-100 ring-offset-2"
            :class={ring:ring}
        >
            <img :src="src" />
        </div>
    </div>
    <div v-else class="avatar placeholder indicator" :class="large ? 'w-24' : 'w-10'">
        <span
            v-if="rank < 3"
            class="indicator-item indicator-start text-xs p-1 badge text-secondary-content rounded-full left-1 font-semibold"
            :class="rank === 0 ? 'bg-yellow-500' : rank === 1 ? 'bg-secondary' : 'bg-amber-700'"
        >
            {{ ordinal(rank + 1) }}
        </span>

        <div class="bg-base-200 text-neutral-content rounded-full w-full">
            <span class="text-secondary" :class="large ? 'text-3xl' : 'text-lg'">
                {{ user.forename.charAt(0) + user.surname.charAt(0) }}
            </span>
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '~/utils/types';

export default {
    props: {
        ring: Boolean,
        large: Boolean,
        rank: Number,
        user: Object as PropType<EliminationUser>
    }
}
</script>
