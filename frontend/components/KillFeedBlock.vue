<template>
    <div class="card bg-base-300 border border-gray-300/20">
        <div class="card-body">
            <h2 class="card-title text-base flex-wrap gap-y-0.5">
                <span>
                    <Avatar :user="item.eliminator" /> {{ item.eliminator.forename }} {{ item.eliminator.surname }}
                </span>
                eliminated
                <span>
                    <Avatar :user="item.eliminated" /> {{ item.eliminated.forename }} {{ item.eliminated.surname }}
                </span>
            </h2>
            <p class="text-secondary text-sm">
                {{ timestamp.toLocaleString({weekday: 'long', month: 'long', day: '2-digit'}) }},
                {{ relativeTime }}
            </p>
        </div>
    </div>
</template>

<script setup lang="ts">
import {DateTime} from 'luxon';
import type {PropType} from 'vue';
import type {EliminationFeedItem} from '@/utils/types';
import {useCurrentTimeStore} from '@/store/time';

const props = defineProps({
    item: {
        type: Object as PropType<EliminationFeedItem>,
        required: true
    }
});

const timestamp = ref(DateTime.fromISO(props.item.timeStamp));
const relativeTime = ref(timestamp.value.toRelative());

// Update relative time on current time tick
const store = useCurrentTimeStore();
store.$subscribe(() => {
    relativeTime.value = timestamp.value.toRelative();
});
</script>
