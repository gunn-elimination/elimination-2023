<template>
    <div
        class="flex items-center justify-center p-6 relative border border-gray-300 dark:border-gray-300/10"
        :class="active ? 'bg-primary text-white' : !item ? 'bg-base-200' : 'bg-base-100'"
    >
        <span class="absolute text-sm top-2.5 left-3" :class="active ? 'text-white' : 'text-secondary'">
            {{ date.day }}
        </span>
        <p v-if="item" class="text-sm">
            <strong v-if="item.announcement">
                {{ item.announcement }}<template v-if="item.restriction">: </template>
            </strong>
            <template v-if="item.restriction">
                <em v-if="item.restrictionType === 'SAFE'">To stay safe,</em>
                <em v-else>To eliminate others,</em>
                {{ lowercaseFirstLetter(item.restriction) }}
            </template>
        </p>
    </div>
</template>

<script setup lang="ts">
import {DateTime} from 'luxon';
import type {PropType} from 'vue';
import type {DayInfo} from '@/utils/calendar';
import {useCurrentTimeStore} from '@/store/time';
import {lowercaseFirstLetter} from '@/utils/strings';

const props = defineProps({
    date: {
        type: Object as PropType<DateTime>,
        required: true
    },
    item: Object as PropType<DayInfo | null>
});

const currentTime = useCurrentTimeStore();
const active = computed(() => currentTime.time && currentTime.time.hasSame && currentTime.time.hasSame(props.date, 'day'));
</script>
