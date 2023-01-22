<template>
    <div
        class="flex items-center justify-center p-6 relative border border-gray-300 dark:border-gray-300/10"
        :class="currentTime.time.hasSame(date, 'day') ? 'bg-primary text-white' : !item ? 'bg-base-200' : 'bg-base-100'"
    >
        <span class="absolute text-sm top-2.5 left-3" :class="currentTime.time.hasSame(date, 'day') ? 'text-white' : 'text-secondary'">
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

<script lang="ts">
import type {PropType} from 'vue';
import type {DayInfo} from '@/utils/calendar';
import {DateTime} from 'luxon';

export default {
    name: "CalendarCell",
    props: {
        date: Object as PropType<DateTime>,
        item: Object as PropType<DayInfo | null>
    }
}
</script>

<script setup lang="ts">
import {useCurrentTimeStore} from '@/store/time';
import {lowercaseFirstLetter} from '@/utils/strings';

const currentTime = useCurrentTimeStore();
</script>
